package seko0716.radius.concert.event.domains

import org.springframework.data.annotation.Id
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Geocode(
    @Id
    val nameForSearch: String,
    val name: String,
    val point: Point
) {

}