package seko0716.radius.concert.event.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.geo.Metrics
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import seko0716.radius.concert.event.domains.Event
import seko0716.radius.concert.event.services.EventService
import java.time.LocalDate

@RestController
@RequestMapping("/api")
class EventsController @Autowired constructor(
    private val eventService: EventService
) {
    @GetMapping("/events")
    fun getAllEvents(
        @RequestParam(
            required = false,
            defaultValue = "50",
            name = "count"
        ) count: Int
    ): Flux<Event> {
        return eventService.getAllEvents(count)
    }

    @GetMapping(
        path = [
            "/events/{currentCity}/{distance}/{metric}",
            "/events/{currentCity}/{distance}/{metric}/{genre}",
            "/events/{currentCity}/{distance}/{metric}/{start}/{end}",
            "/events/{currentCity}/{distance}/{metric}/{genre}/{start}/{end}"
        ]
    )
    fun getEvents(
        @PathVariable(name = "currentCity") currentCity: String,
        @PathVariable(name = "distance") distance: Double,
        @PathVariable(name = "metric") metric: Metrics,
        @PathVariable(required = false, name = "genre") genre: String?,
        @PathVariable(required = false, name = "start") @DateTimeFormat(pattern = "ddMMyy") start: LocalDate?,
        @PathVariable(required = false, name = "end") @DateTimeFormat(pattern = "ddMMyy") end: LocalDate?
    ): Flux<Event> {
        return eventService.getEvents(currentCity, distance, metric, genre, start, end)
    }
}