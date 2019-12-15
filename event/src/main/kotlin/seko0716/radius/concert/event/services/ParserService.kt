package seko0716.radius.concert.event.services

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
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
    suspend fun updateData(): Flow<Event> = coroutineScope {
        val start = System.nanoTime()
        val events = cityParsers
            .map { async { it.parse() } }
            .flatMap { it.await() }
            .groupBy { it.type }
            .map { entry ->
                async {
                    val parser = eventParsers.find { it.type() == entry.key }
                    entry.value.map { async { parser?.parse(it) ?: listOf() } }.flatMap { it.await() }
                }
            }
            .flatMap { it.await() }
        val end = System.nanoTime()
        println(end - start)
//        eventRepository.saveAll(events)
        listOf<Event>().asFlow()
    }
}