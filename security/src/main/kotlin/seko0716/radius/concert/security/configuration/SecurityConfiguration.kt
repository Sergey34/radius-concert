package seko0716.radius.concert.security.configuration

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import seko0716.radius.concert.security.domains.Role
import seko0716.radius.concert.security.domains.User
import seko0716.radius.concert.security.service.MongoUserDetailService


@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfiguration {
    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity, userDetailService: MongoUserDetailService
    ): SecurityWebFilterChain {
        return http
            .authenticationManager(authenticationManager(userDetailService))
            .httpBasic()
            .and()
            .authorizeExchange()
            .pathMatchers("/api/admin/update").hasAuthority("Admin")
            .anyExchange()
            .permitAll()
            .and()
            .build()
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun defaultUser(userRepository: MongoTemplate): User {
        userRepository.findAllAndRemove<User>(Query.query(Criteria.where("login").`is`("seko")))
        return User(
            socialAccountId = "",
            authServiceType = "",
            login = "seko",
            pass = passwordEncoder().encode("0716"),
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
            .apply { setPasswordEncoder(passwordEncoder()) }
    }
}