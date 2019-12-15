package seko0716.radius.concert.event.domains

data class Coordinate(
    val latt: Double,
    val longt: Double
) {
    fun isNan() = longt.isNaN() || latt.isNaN()
}