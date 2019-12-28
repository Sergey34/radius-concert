package seko0716.radius.concert.admin.services.parsers.kassir

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
//import org.mockito.ArgumentMatchers.anyObject
//import org.mockito.ArgumentMatchers.notNull
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.data.geo.Point
import seko0716.radius.concert.event.services.GeocodeService
import seko0716.radius.concert.event.services.MongodbGeocodeService

internal class KassirCityParserTest {
    companion object {
        var geocodeService: GeocodeService = Mockito.mock(MongodbGeocodeService::class.java)
        @BeforeAll
        @JvmStatic
        @Suppress("unused")
        fun before() = runBlocking {
            @Suppress("UNUSED_VARIABLE")
            val thenReturn = `when`(
                geocodeService.getGeocode(anyObject())
            ).thenReturn(Point(Double.NaN, Double.NaN))
        }

        private fun <T> anyObject(): T {
            Mockito.any<T>()
            return uninitialized()
        }

        @Suppress("UNCHECKED_CAST", "unused")
        private fun <T> uninitialized(): T = null as T
    }

    @Test
    fun parse() = runBlocking {
        val kassirCityParser = KassirCityParser(geocodeService)
        val parse = kassirCityParser.parse()
        Assertions.assertTrue(parse.isNotEmpty())
    }
}