package seko0716.radius.concert.geocode.domains

import org.springframework.data.annotation.Id
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Geocode(
    @Id
    val nameForSearch: String,
    val name: String,
    val point: MyPoint
) {
    companion object {
        @JvmField
        val EMPTY_GEOCODE =
            Geocode("", "", MyPoint(Double.NaN, Double.NaN))
    }
}

class MyPoint(x: Double = Double.NaN, y: Double = Double.NaN) : Point(x, y)