package seko0716.radius.concert.event.services

import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service
import seko0716.radius.concert.event.domains.Event
import seko0716.radius.concert.event.repository.EventRepository
import seko0716.radius.concert.shared.exceptions.CityNotFoundException
import seko0716.radius.concert.shared.isNan

@Service
class EventService @Autowired constructor(
    private val eventRepository: EventRepository,
    private val geocodeService: GeocodeService,
    val mongoTemplate: MongoTemplate
) {
    suspend fun getAllEvents(count: Int): Flow<Event> {
        return eventRepository.getEvents(count)
    }

    suspend fun getEvents(
        city: String,
        distance: Double,
        metric: Metrics
    ): Flow<Event> {
        return geocodeService.getGeocodeById(city.toLowerCase()).run {
            if (point.isNan()) throw CityNotFoundException(city)
            eventRepository.getEvents(point, Distance(distance, metric))
        }
    }
}