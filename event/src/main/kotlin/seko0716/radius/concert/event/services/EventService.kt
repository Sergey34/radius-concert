package seko0716.radius.concert.event.services

import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import seko0716.radius.concert.event.domains.Event
import seko0716.radius.concert.event.repository.EventRepository

@Service
class EventService @Autowired constructor(
    private val eventRepository: EventRepository
) {
    suspend fun getAllEvents(): Flow<Event> {
        return eventRepository.getAllEvents()
    }
}