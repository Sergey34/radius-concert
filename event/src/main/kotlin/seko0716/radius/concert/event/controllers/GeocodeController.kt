package seko0716.radius.concert.event.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.geo.Point
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import seko0716.radius.concert.event.services.GeocodeService
import java.lang.Double.NaN

@RestController
@RequestMapping("/api")
class GeocodeController @Autowired constructor(
    private val geocodeService: GeocodeService
) {
    // todo temporary solution
    @PostMapping("/geocode", consumes = ["application/json"])
    suspend fun geocode(@RequestBody geocodes: Geocode) {
        geocodeService.addGeocodes(geocodes.geocodes)
    }
}

data class Geocode(
    val geocodes: List<GeocodeInfo>
)

data class GeocodeInfo(
    val description: String,
    val name: String,
    val cityName: String = "$name, $description",
    val point: MyPoint
)

class MyPoint(
    x: Double = NaN,
    y: Double = NaN
) : Point(x, y)