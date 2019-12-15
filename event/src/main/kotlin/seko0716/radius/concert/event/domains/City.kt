package seko0716.radius.concert.event.domains

import org.springframework.data.geo.Point

data class City(
    val type: String,
    val url: String,
    val name: String,
    val position: Point
)