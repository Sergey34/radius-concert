package seko0716.radius.concert.event.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import seko0716.radius.concert.event.services.parsers.CityParser
import seko0716.radius.concert.event.services.parsers.EventParser

@Service
class ParserService @Autowired constructor(
    val cityParsers: List<CityParser>,
    val eventParsers: List<EventParser>
) {
    @Scheduled(cron = "* * * * * *")
    suspend fun updateData() {
        val events = cityParsers
            .flatMap { it.parse() }
            .flatMap { c -> eventParsers.find { it.type() == c.type }?.parse(c) ?: listOf() }
    }
}