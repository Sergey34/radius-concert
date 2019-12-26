package seko0716.radius.concert.event.services

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapConcat
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
    @FlowPreview
    suspend fun updateData(): Flow<Event> = coroutineScope {
        val events = cityParsers
            .map { async { it.parse() } }
            .flatMap { it.await() }
            .groupBy { it.type }
            .map { entry ->
                async {
                    eventParsers.find { it.type() == entry.key }?.run {
                        eventRepository.saveAll(entry.value.map { async { parse(it) } }.flatMap { it.await() })
                    } ?: emptyFlow()
                }
            }
            .asFlow()
            .flatMapConcat { it.await() }
        events
    }
}