package seko0716.radius.concert.security.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findAllAndRemove
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.DefaultServerRedirectStrategy
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.ServerRedirectStrategy
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.security.web.server.savedrequest.ServerRequestCache
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache
import reactor.core.publisher.Mono
import seko0716.radius.concert.security.domains.Role
import seko0716.radius.concert.security.domains.User
import seko0716.radius.concert.security.service.MongoUserDetailService
import java.net.URI


@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfiguration {

    class MyServerAuthenticationSuccessHandler : ServerAuthenticationSuccessHandler {
        companion object {
            @JvmField
            val location: URI = URI.create("/")
        }

        private val redirectStrategy: ServerRedirectStrategy = DefaultServerRedirectStrategy()
        private val requestCache: ServerRequestCache = WebSessionServerRequestCache()
        override fun onAuthenticationSuccess(
            webFilterExchange: WebFilterExchange,
            authentication: Authentication
        ): Mono<Void> {
            println()
            val user = authentication.principal as User
            return if (user.enabled) {
                this.requestCache.getRedirectUri(webFilterExchange.exchange)
                    .defaultIfEmpty(location)
                    .flatMap { location: URI ->
                        redirectStrategy.sendRedirect(
                            webFilterExchange.exchange,
                            location
                        )
                    }
            } else {
                redirectStrategy.sendRedirect(webFilterExchange.exchange, URI.create("/fill_password_page"))
            }
        }
    }

    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity, userDetailService: MongoUserDetailService
    ): SecurityWebFilterChain {
        return http
            .oauth2Login()
            .authenticationSuccessHandler(MyServerAuthenticationSuccessHandler())
            .and()
            .formLogin()
            .authenticationManager(authenticationManager(userDetailService))
            .and()
            .httpBasic()
            .and()
            .authorizeExchange()
            .pathMatchers("/api/admin/update").hasAuthority("Admin")
//            .pathMatchers("/ttt").authenticated()
            .anyExchange().permitAll()
            .and()
            .csrf().disable()
            .build()
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun defaultUser(
        userRepository: MongoTemplate,
        @Value("\${spring.security.user.name}") name: String,
        @Value("\${spring.security.user.password}") password: String
    ): User {
        userRepository.findAllAndRemove<User>(Query.query(Criteria.where("login").`is`(name)))
        return User(
            socialAccountId = "",
            authServiceType = "",
            login = name,
            pass = passwordEncoder().encode(password),
            firstName = "",
            roles = listOf(Role("Admin"))
        ).apply {
            val save = userRepository.save(this)
            println(save)
        }
    }

    @Bean
    fun authenticationManager(userDetailService: MongoUserDetailService): ReactiveAuthenticationManager {
        return UserDetailsRepositoryReactiveAuthenticationManager(userDetailService)
            .apply {
                setPasswordEncoder(passwordEncoder())
            }
    }
}