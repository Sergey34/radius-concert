package seko0716.radius.concert.security.service

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import seko0716.radius.concert.security.controller.Password
import seko0716.radius.concert.security.domains.User
import seko0716.radius.concert.security.repository.UserRepository

@Service
class MongoUserDetailService constructor(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) : ReactiveUserDetailsService {
    override fun findByUsername(username: String): Mono<UserDetails> {
        return userRepository.findUserByLogin(username).map { it as UserDetails }
    }

    fun getUserByUsername(username: String): Mono<User> {
        return userRepository.findUserByLogin(username)
    }

    fun enabledOrNull(user: User?): Boolean {
        return user?.isEnabled ?: true
    }

    fun createUser(user: User): Mono<User> {
        return userRepository.save(user)
    }

    fun update(user: User): Mono<User> {
        return userRepository.save(user)
    }

    fun updatePassword(user: User, password: Password): Mono<Unit> {
        user.enabled = true
        user.pass = passwordEncoder.encode(password.psw)
        return update(user)
            .flatMap { u ->
                ReactiveSecurityContextHolder.getContext()
                    .map { Pair<SecurityContext, Authentication>(it, it.authentication) }
                    .map {
                        it.first.authentication = when (val authentication = it.second) {
                            is OAuth2AuthenticationToken ->
                                OAuth2AuthenticationToken(
                                    u,
                                    u.authorities,
                                    authentication.authorizedClientRegistrationId
                                )
                            is UsernamePasswordAuthenticationToken ->
                                UsernamePasswordAuthenticationToken(
                                    u,
                                    authentication.credentials,
                                    authentication.authorities
                                )
                            else -> throw IllegalStateException()
                        }
                    }
            }


    }
}