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
import seko0716.radius.concert.security.domains.DtoUser
import seko0716.radius.concert.security.domains.Role
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

    @Suppress("unused")
    fun enabledOrNull(user: User?): Boolean {
        return user?.isEnabled ?: true
    }

    fun createUser(dtoUser: DtoUser): Mono<User> {
        val user = dtoUser.takeIf {
            it.login != null && it.login.isNotBlank()
                    && it.isValid() && it.pwd != null && it.pwd.isNotBlank()
        }
            ?.let {
                User(
                    login = it.login!!,
                    pass = passwordEncoder.encode(it.pwd),
                    roles = listOf(Role("USER")),
                    firstName = it.login,
                    enabled = true
                )
            }
            ?.run {
                userRepository.save(this)
            }
        return user ?: throw IllegalArgumentException("Invalid dto user")
    }

    fun update(user: User): Mono<User> {
        return userRepository.save(user)
    }

    fun updateUser(user: User, dtoUser: DtoUser): Mono<*> {
        if (!dtoUser.isValid()) {
            return Mono.error<IllegalArgumentException>(IllegalArgumentException("Invalid dto user"))
        }
        return update(fillUserParams(user, dtoUser))
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
                                    u.password,
                                    authentication.authorities
                                )
                            else -> throw IllegalStateException()
                        }
                    }
            }
    }

    private fun fillUserParams(
        user: User,
        dtoUser: DtoUser
    ): User {
        user.enabled = true
        user.pass = dtoUser.pwd?.takeIf { it.isNotBlank() }?.let { passwordEncoder.encode(it) } ?: user.pass
        user.email = dtoUser.email
        user.firstName = dtoUser.firstName
        user.lastName = dtoUser.lastName
        return user
    }
}