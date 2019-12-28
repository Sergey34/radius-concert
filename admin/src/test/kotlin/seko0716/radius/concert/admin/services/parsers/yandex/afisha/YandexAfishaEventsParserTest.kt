package seko0716.radius.concert.admin.services.parsers.yandex.afisha

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import seko0716.radius.concert.event.domains.City

internal class YandexAfishaEventsParserTest {


    @Test
    fun parse() = runBlocking {
        val parse = YandexAfishaEventsParser(jacksonObjectMapper()).parse(
            City(
                "", "https://saratov.kassir.ru/", "", GeoJsonPoint(
                    Point(
                        Double.NaN,
                        Double.NaN
                    )
                ),
                "saratov"
            )
        )
        assertTrue(parse.isNotEmpty())
    }
}