package seko0716.radius.concert.security.service

import com.nimbusds.oauth2.sdk.ErrorObject
import com.nimbusds.openid.connect.sdk.UserInfoErrorResponse
import net.minidev.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService
import org.springframework.security.oauth2.core.AuthenticationMethod
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component
import org.springframework.util.Assert
import org.springframework.util.StringUtils
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import seko0716.radius.concert.security.domains.User
import java.io.IOException
import java.util.*

@Component
class PersistentReactiveOAuth2UserService @Autowired constructor(
    private val userDetailService: MongoUserDetailService
) : ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private var webClient = WebClient.create()

    override fun loadUser(userRequest: OAuth2UserRequest): Mono<OAuth2User> {
        return Mono.defer {
            Assert.notNull(userRequest, "userRequest cannot be null")
            val userInfoUri = userRequest.clientRegistration.providerDetails
                .userInfoEndpoint.uri
            if (!StringUtils.hasText(
                    userInfoUri
                )
            ) {
                val oauth2Error =
                    OAuth2Error(
                        MISSING_USER_INFO_URI_ERROR_CODE,
                        "Missing required UserInfo Uri in UserInfoEndpoint for Client Registration: "
                                + userRequest.clientRegistration.registrationId,
                        null
                    )
                throw OAuth2AuthenticationException(oauth2Error, oauth2Error.toString())
            }
            val userNameAttributeName =
                userRequest.clientRegistration.providerDetails.userInfoEndpoint
                    .userNameAttributeName
            if (!StringUtils.hasText(userNameAttributeName)) {
                val oauth2Error =
                    OAuth2Error(
                        MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE,
                        "Missing required \"user name\" attribute name in UserInfoEndpoint for Client Registration: "
                                + userRequest.clientRegistration.registrationId,
                        null
                    )
                throw OAuth2AuthenticationException(oauth2Error, oauth2Error.toString())
            }
            val typeReference: ParameterizedTypeReference<Map<String, Any>> =
                object : ParameterizedTypeReference<Map<String, Any>>() {}
            val authenticationMethod = userRequest.clientRegistration.providerDetails
                .userInfoEndpoint.authenticationMethod
            val requestHeadersSpec: WebClient.RequestHeadersSpec<*>
            requestHeadersSpec = if (AuthenticationMethod.FORM == authenticationMethod) {
                webClient.post()
                    .uri(userInfoUri)
                    .header(
                        HttpHeaders.ACCEPT,
                        MediaType.APPLICATION_JSON_VALUE
                    )
                    .header(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_FORM_URLENCODED_VALUE
                    )
                    .bodyValue("access_token=" + userRequest.accessToken.tokenValue)
            } else {
                webClient.get()
                    .uri(userInfoUri)
                    .header(
                        HttpHeaders.ACCEPT,
                        MediaType.APPLICATION_JSON_VALUE
                    )
                    .headers { headers: HttpHeaders ->
                        headers.setBearerAuth(
                            userRequest.accessToken.tokenValue
                        )
                    }
            }
            requestHeadersSpec
                .retrieve()
                .onStatus(
                    { s: HttpStatus -> s != HttpStatus.OK }
                ) { response: ClientResponse ->
                    parse(
                        response
                    ).map<Throwable> { userInfoErrorResponse: UserInfoErrorResponse ->
                        val description = userInfoErrorResponse.errorObject.description
                        val oauth2Error =
                            OAuth2Error(
                                INVALID_USER_INFO_RESPONSE_ERROR_CODE,
                                description,
                                null
                            )
                        throw OAuth2AuthenticationException(
                            oauth2Error,
                            oauth2Error.toString()
                        )
                    }
                }
                .bodyToMono(typeReference)
                .flatMap { attrs: Map<String, Any> ->
                    val authority: GrantedAuthority = SimpleGrantedAuthority("USER")
                    val authorities: MutableSet<GrantedAuthority> = HashSet()
                    authorities.add(authority)
                    val token = userRequest.accessToken
                    for (scope in token.scopes) {
                        authorities.add(SimpleGrantedAuthority("SCOPE_$scope"))
                    }

                    val login = attrs[userNameAttributeName] as String
                    val user = userDetailService.getUserByUsername(login)
                        .defaultIfEmpty(
                            User(
                                socialAccountId = attrs["sub"] as String,
                                authServiceType = "google",
                                email = login,
                                firstName = attrs["given_name"] as String,
                                lastName = attrs["family_name"] as String,
                                login = login,
                                roles = authorities.toList(),
                                pass = "",
                                attrs = attrs,
                                enabled = false
                            )
                        )
                        .map { if (!it.enabled) userDetailService.update(it) else Mono.just(it) }
                        .flatMap { it }
                    user
                }
                .onErrorMap(
                    { e: Throwable? -> e is IOException }
                ) { t: Throwable? ->
                    AuthenticationServiceException(
                        "Unable to access the userInfoEndpoint $userInfoUri",
                        t
                    )
                }
                .onErrorMap(
                    { t: Throwable? -> t !is AuthenticationServiceException }
                ) { t: Throwable ->
                    val oauth2Error =
                        OAuth2Error(
                            INVALID_USER_INFO_RESPONSE_ERROR_CODE,
                            "An error occurred reading the UserInfo Success response: " + t.message,
                            null
                        )
                    OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), t)
                }
        }
    }

    /**
     * Sets the [WebClient] used for retrieving the user endpoint
     * @param webClient the client to use
     */
    fun setWebClient(webClient: WebClient) {
        Assert.notNull(webClient, "webClient cannot be null")
        this.webClient = webClient
    }

    companion object {
        private const val INVALID_USER_INFO_RESPONSE_ERROR_CODE = "invalid_user_info_response"
        private const val MISSING_USER_INFO_URI_ERROR_CODE = "missing_user_info_uri"
        private const val MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE = "missing_user_name_attribute"
        private fun parse(httpResponse: ClientResponse): Mono<UserInfoErrorResponse> {
            val wwwAuth =
                httpResponse.headers().asHttpHeaders().getFirst(HttpHeaders.WWW_AUTHENTICATE)
            if (!StringUtils.isEmpty(wwwAuth)) { // Bearer token error?
                return Mono.fromCallable {
                    UserInfoErrorResponse.parse(
                        wwwAuth
                    )
                }
            }
            val typeReference: ParameterizedTypeReference<Map<String, String>> =
                object : ParameterizedTypeReference<Map<String, String>>() {}
            // Other error?
            return httpResponse
                .bodyToMono(typeReference)
                .map { body: Map<String, String?>? ->
                    UserInfoErrorResponse(
                        ErrorObject.parse(JSONObject(body))
                    )
                }
        }
    }
}
