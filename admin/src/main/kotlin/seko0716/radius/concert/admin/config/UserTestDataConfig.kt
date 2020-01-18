package seko0716.radius.concert.admin.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findAllAndRemove
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import seko0716.radius.concert.security.domains.Role
import seko0716.radius.concert.security.domains.User

@Configuration
class UserTestDataConfig @Autowired constructor(
    val passwordEncoder: BCryptPasswordEncoder
) {
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
            email = "asd@mail.com",
            pass = passwordEncoder.encode(password),
            firstName = "",
            roles = listOf(Role("Admin"))
        ).apply {
            val save = userRepository.save(this)
            println(save)
        }
    }
}