package seko0716.radius.concert.security.configuration

import org.springframework.security.core.Authentication
import org.springframework.security.web.server.DefaultServerRedirectStrategy
import org.springframework.security.web.server.ServerRedirectStrategy
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.security.web.server.savedrequest.ServerRequestCache
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache
import reactor.core.publisher.Mono
import seko0716.radius.concert.security.domains.User
import java.net.URI

class FillPasswordPageForDisabledUsersServerAuthenticationSuccessHandler : ServerAuthenticationSuccessHandler {
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
            redirectStrategy.sendRedirect(webFilterExchange.exchange, URI.create("/account"))
        }
    }
}