package seko0716.radius.concert.event.contrallsers

import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import seko0716.radius.concert.event.domains.Event
import seko0716.radius.concert.event.services.EventService
import seko0716.radius.concert.event.services.ParserService

@RestController
class EventsController @Autowired constructor(
    private val eventService: EventService,
    private val parserService: ParserService
) {
    @GetMapping("/events")
    suspend fun getAllEvents(): Flow<Event> {
        return eventService.getAllEvents()
    }

    @GetMapping("/update")
    suspend fun updateData(): Flow<Event> {
        return parserService.updateData()
    }
}