package seko0716.radius.concert.configurationservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.config.server.EnableConfigServer

@EnableConfigServer
@SpringBootApplication
class ConfigurationServiceApplication

fun main(args: Array<String>) {
    runApplication<ConfigurationServiceApplication>(*args)
}
