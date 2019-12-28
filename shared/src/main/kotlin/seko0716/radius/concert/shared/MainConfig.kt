package seko0716.radius.concert.shared

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableCaching
@Configuration
class MainConfig {
    @Bean
    fun objectMapper() = jacksonObjectMapper()
}