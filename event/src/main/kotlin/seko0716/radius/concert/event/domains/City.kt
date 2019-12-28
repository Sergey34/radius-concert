package seko0716.radius.concert.event.domains

import org.springframework.data.mongodb.core.geo.GeoJsonPoint

data class City(
    val type: String = "N/A",
    val url: String,
    val name: String,
    val position: GeoJsonPoint = GeoJsonPoint(Double.NaN, Double.NaN),
    val id: String = name.toLowerCase()
)