package seko0716.radius.concert.admin.services

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import seko0716.radius.concert.admin.services.parsers.CityParser
import seko0716.radius.concert.admin.services.parsers.EventParser
import seko0716.radius.concert.event.domains.Event
import seko0716.radius.concert.event.repository.EventRepository

@Service
class ParserService @Autowired constructor(
    val cityParser: CityParser,
    val eventParser: EventParser,
    val eventRepository: EventRepository
) {
    @FlowPreview
    suspend fun updateData(): Flow<Event> = coroutineScope {
        val events = cityParser.parse()
            .map {
                async {
                    eventRepository.saveAllSync(eventParser.parse(it))
                }
            }.asFlow().flatMapConcat { it.await() }
        events
    }
}