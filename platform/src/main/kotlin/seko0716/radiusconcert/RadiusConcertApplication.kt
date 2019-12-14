package seko0716.radiusconcert

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import seko0716.radius.concert.event.services.EventService

@SpringBootApplication
class RadiusConcertApplication

fun main(args: Array<String>) {
    val t = EventService()
    runApplication<RadiusConcertApplication>(*args)
}