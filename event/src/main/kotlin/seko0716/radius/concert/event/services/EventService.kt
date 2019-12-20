package seko0716.radius.concert.event.services

import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics
import org.springframework.data.geo.Point
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import seko0716.radius.concert.event.config.isNan
import seko0716.radius.concert.event.domains.Event
import seko0716.radius.concert.event.repository.EventRepository

@Service
class EventService @Autowired constructor(
    private val eventRepository: EventRepository,
    private val geocodeService: GeocodeService
) {
    suspend fun getAllEvents(count: Int): Flow<Event> {
        return eventRepository.getEvents(count)
    }

    suspend fun getEvents(
        city: String,
        distance: Double,
        metric: Metrics,
        point: Point
    ): Flow<Event> {
        return if (!point.isNan()) {
            eventRepository.getEvents(point, Distance(distance, metric))
        } else {
            geocodeService.getGeocode(city).run {
                if (!isNan()) {
                    eventRepository.getEvents(this, Distance(distance, metric))
                } else {
                    throw CityNotFoundException(city)
                }
            }
        }
    }
}

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "City not found")
class CityNotFoundException(city: String) : RuntimeException("City '${city}'not found")
