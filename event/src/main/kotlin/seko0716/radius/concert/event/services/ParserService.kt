package seko0716.radius.concert.event.services

import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import seko0716.radius.concert.event.domains.Event
import seko0716.radius.concert.event.repository.EventRepository
import seko0716.radius.concert.event.services.parsers.CityParser
import seko0716.radius.concert.event.services.parsers.EventParser

@Service
class ParserService @Autowired constructor(
    val cityParsers: List<CityParser>,
    val eventParsers: List<EventParser>,
    val eventRepository: EventRepository
) {
    //    @Scheduled(cron = "\${spring.application.events.dataset.refresh}")
    suspend fun updateData(): Flow<Event> {
        val events = cityParsers
            .flatMap { it.parse() }
            .flatMap { c -> eventParsers.find { it.type() == c.type }?.parse(c) ?: listOf() }
        return eventRepository.saveAll(events)
    }
}