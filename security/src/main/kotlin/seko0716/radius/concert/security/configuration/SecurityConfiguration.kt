package seko0716.radius.concert.security.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
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
            .oauth2Login()
            .authenticationSuccessHandler(FillPasswordPageForDisabledUsersServerAuthenticationSuccessHandler())
            .and()
            .formLogin()
            .loginPage("/login")
            .authenticationManager(authenticationManager(userDetailService))
            .and()
            .httpBasic()
            .and()
            .authorizeExchange()
            .pathMatchers("/api/admin/update").hasAuthority("Admin")
            .pathMatchers("/account").authenticated()
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
    fun authenticationManager(userDetailService: MongoUserDetailService): ReactiveAuthenticationManager {
        return UserDetailsRepositoryReactiveAuthenticationManager(userDetailService)
            .apply {
                setPasswordEncoder(passwordEncoder())
            }
    }
}