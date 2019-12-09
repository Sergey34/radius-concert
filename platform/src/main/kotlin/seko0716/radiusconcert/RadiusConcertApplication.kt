package seko0716.radiusconcert

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import seko0716.radius.concert.concert.T

@SpringBootApplication
class RadiusConcertApplication

fun main(args: Array<String>) {
    val t = T()
    runApplication<RadiusConcertApplication>(*args)
}