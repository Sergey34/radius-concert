package seko0716.radius.concert.event.controllers

import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import seko0716.radius.concert.event.domains.Geocode
import seko0716.radius.concert.event.services.GeocodeService

@RestController
@RequestMapping("/api")
class GeocodeController @Autowired constructor(
    private val geocodeService: GeocodeService
) {
    @GetMapping("/geocodes/{template}")
    suspend fun searchGeocode(@PathVariable("template") template: String): Flow<Geocode> {
        return geocodeService.searchGeocodes(template)
    }
}