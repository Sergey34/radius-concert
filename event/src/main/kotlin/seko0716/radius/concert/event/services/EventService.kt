package seko0716.radius.concert.event.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seko0716.radius.concert.event.domains.Event
import seko0716.radius.concert.event.repository.EventRepository
import seko0716.radius.concert.shared.exceptions.CityNotFoundException
import seko0716.radius.concert.shared.isNan
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class EventService @Autowired constructor(
    private val eventRepository: EventRepository,
    private val geocodeService: GeocodeService
) {
    fun getAllEvents(count: Int): Flux<Event> {
        return eventRepository.getEvents(count)
    }

    fun getEvents(
        city: String,
        distance: Double,
        metric: Metrics,
        genre: String?,
        start: LocalDate?,
        end: LocalDate?
    ): Flux<Event> {
        return geocodeService.getGeocodeById(city.toLowerCase())
            .flatMapMany {
                if (it.point.isNan()) throw CityNotFoundException(city)
                eventRepository.getEvents(
                    it.point, Distance(distance, metric), genre,
                    start?.let { d ->
                        LocalDateTime.of(
                            d,
                            LocalTime.MIN
                        )
                    },
                    end?.let { d ->
                        LocalDateTime.of(
                            d,
                            LocalTime.MAX
                        )
                    }
                )
            }
    }

    fun getEvent(id: String): Mono<Event> {
        return eventRepository.findById(id)
    }

    fun getEventsByCity(city: String): Flux<Event> {
        return eventRepository.getEventsByCity(city)
    }
}