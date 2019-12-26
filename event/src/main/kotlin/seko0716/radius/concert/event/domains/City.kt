package seko0716.radius.concert.event.domains

import org.springframework.data.mongodb.core.geo.GeoJsonPoint

data class City(
    val type: String,
    val url: String,
    val name: String,
    val position: GeoJsonPoint
)