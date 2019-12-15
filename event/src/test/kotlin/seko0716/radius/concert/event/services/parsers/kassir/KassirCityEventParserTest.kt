package seko0716.radius.concert.event.services.parsers.kassir

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import seko0716.radius.concert.event.services.FileGeocodeService

internal class KassirCityEventParserTest {
    companion object {
        lateinit var geocodeService: FileGeocodeService
        @BeforeAll
        @JvmStatic
        fun before() = runBlocking {
            geocodeService =
                FileGeocodeService("src/main/resources/geocodes.xml")
            geocodeService.initGeocodes()
        }
    }

    @Test
    fun parse() = runBlocking {
        val kassirCityParser = KassirCityParser(geocodeService)
        val parse = kassirCityParser.parse()
        Assertions.assertTrue(parse.isNotEmpty())
    }
}