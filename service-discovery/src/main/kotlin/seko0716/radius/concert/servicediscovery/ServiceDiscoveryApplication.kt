package seko0716.radius.concert.servicediscovery

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@EnableEurekaServer
@SpringBootApplication
class ServiceDiscoveryApplication

fun main(args: Array<String>) {
    runApplication<ServiceDiscoveryApplication>(*args)
}
