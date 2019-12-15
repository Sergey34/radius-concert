package seko0716.radius.concert.event.services.parsers

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import seko0716.radius.concert.event.services.FileGeocodeService

internal class GeocodeServiceTest {

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
    fun getGeocodeSaratov() = runBlocking {
        val geocode = geocodeService.getGeocode("Саратов")
        assertTrue(!geocode.isNan())
    }

    @Test
    fun getGeocodeMoscow() = runBlocking {
        val geocode = geocodeService.getGeocode("Москва")
        assertTrue(!geocode.isNan())
    }
}