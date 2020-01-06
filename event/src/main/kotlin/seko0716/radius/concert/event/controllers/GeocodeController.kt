package seko0716.radius.concert.event.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seko0716.radius.concert.event.domains.Geocode
import seko0716.radius.concert.event.services.GeocodeService

@RestController
@RequestMapping("/api")
class GeocodeController @Autowired constructor(
    private val geocodeService: GeocodeService
) {
    @GetMapping("/geocodes/{template}")
    fun searchGeocode(@PathVariable("template") template: String): Flux<Geocode> {
        return geocodeService.searchGeocodes(template)
    }

    @GetMapping("/geocode/{template}")
    fun getGeocode(@PathVariable("template") template: String): Mono<Geocode> {
        return geocodeService.getGeocodeById(template)
    }
}