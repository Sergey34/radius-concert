package seko0716.radius.concert.geocode.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seko0716.radius.concert.geocode.domains.Geocode
import seko0716.radius.concert.geocode.services.GeocodeService

@RestController
@RequestMapping("/api")
class GeocodeController @Autowired constructor(
    private val geocodeService: GeocodeService
) {

    @GetMapping("/geocodes/{template}")
    fun searchGeocode(@PathVariable("template") template: String): Flux<Geocode> {
        return geocodeService.searchGeocodes(template)
    }

    @GetMapping("/geocode/{id}")
    fun getGeocode(@PathVariable("id") template: String): Mono<Geocode> {
        return geocodeService.getGeocodeById(template)
    }
}