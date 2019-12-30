package seko0716.radius.concert.security.service

import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import seko0716.radius.concert.security.repository.UserRepository

@Service
class MongoUserDetailService constructor(
    private val userRepository: UserRepository
) : ReactiveUserDetailsService {
    override fun findByUsername(username: String): Mono<UserDetails> {
        return userRepository.findUserByLogin(username)
    }
}