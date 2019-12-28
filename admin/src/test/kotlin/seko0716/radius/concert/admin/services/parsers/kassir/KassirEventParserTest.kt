package seko0716.radius.concert.admin.services.parsers.kassir

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import seko0716.radius.concert.event.domains.City

internal class KassirEventParserTest {

    @Test
    fun parse() = runBlocking {
        val parse = KassirEventParser(jacksonObjectMapper()).parse(
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