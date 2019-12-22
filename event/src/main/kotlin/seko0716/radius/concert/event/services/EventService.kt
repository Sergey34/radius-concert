package seko0716.radius.concert.event.services

import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics
import org.springframework.stereotype.Service
import seko0716.radius.concert.event.config.isNan
import seko0716.radius.concert.event.domains.Event
import seko0716.radius.concert.event.repository.EventRepository
import seko0716.radius.concert.event.services.exceptions.CityNotFoundException

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
        searchType: String
    ): Flow<Event> {
        val formattedCity = if (searchType == "Город") "россия, $city" else city
        return geocodeService.getGeocode(formattedCity).run {
            if (isNan()) throw CityNotFoundException(city)
            eventRepository.getEvents(this, Distance(distance, metric))
        }
    }
}