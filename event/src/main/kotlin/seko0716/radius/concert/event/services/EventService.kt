package seko0716.radius.concert.event.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics
import org.springframework.stereotype.Service
import seko0716.radius.concert.event.config.isNan
import seko0716.radius.concert.event.domains.Event
import seko0716.radius.concert.event.repository.EventRepository

@Service
class EventService @Autowired constructor(
    private val eventRepository: EventRepository,
    private val geocodeService: GeocodeService
) {
    suspend fun getAllEvents(): Flow<Event> {
        return eventRepository.getAllEvents()
    }

    suspend fun getEvents(city: String, distance: Double, metric: Metrics): Flow<Event> {
        return geocodeService.getGeocode(city).run {
            if (!isNan()) {
                eventRepository.getEvents(this, Distance(distance, metric))
            } else {
                emptyFlow()
            }
        }
    }
}