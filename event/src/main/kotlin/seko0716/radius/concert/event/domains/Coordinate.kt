package seko0716.radius.concert.event.domains

import org.springframework.data.geo.Point

data class Coordinate(
    val latt: Double,
    val longt: Double
) : Point(latt, longt) {
    fun isNan() = longt.isNaN() || latt.isNaN()
}