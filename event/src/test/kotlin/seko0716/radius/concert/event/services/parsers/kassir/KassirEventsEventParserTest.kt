package seko0716.radius.concert.event.services.parsers.kassir

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import seko0716.radius.concert.event.domains.City

internal class KassirEventsEventParserTest {

    @Test
    fun parse() = runBlocking {
        val parse = KassirEventsEventParser().parse(
            City(
                "", "https://saratov.kassir.ru/", "", GeoJsonPoint(
                    Point(
                        Double.NaN,
                        Double.NaN
                    )
                )
            )
        )
        assertTrue(parse.isNotEmpty())
    }
}