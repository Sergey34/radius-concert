package seko0716.radius.concert.event.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableCaching
@Configuration
class EventMainConfig {
    @Bean
    fun objectMapper() = jacksonObjectMapper()
}