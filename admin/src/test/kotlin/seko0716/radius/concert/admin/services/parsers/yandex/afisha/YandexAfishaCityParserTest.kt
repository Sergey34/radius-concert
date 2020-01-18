package seko0716.radius.concert.admin.services.parsers.yandex.afisha

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import reactor.core.publisher.Mono
import seko0716.radius.concert.geocode.domains.Geocode
import seko0716.radius.concert.geocode.services.GeocodeService
import seko0716.radius.concert.geocode.services.MongodbGeocodeService

@Suppress("UNCHECKED_CAST")
internal class YandexAfishaCityParserTest {
    companion object {
        var geocodeService: GeocodeService = Mockito.mock(MongodbGeocodeService::class.java)
        @BeforeAll
        @JvmStatic
        @Suppress("unused")
        fun before() = runBlocking {
            @Suppress("UNUSED_VARIABLE")
            val thenReturn = Mockito.`when`(
                geocodeService.getGeocodeById(anyObject())
            ).thenReturn(Mono.just(Geocode.EMPTY_GEOCODE))
        }

        private fun <T> anyObject(): T {
            Mockito.any<T>()
            return uninitialized()
        }

        private fun <T> uninitialized(): T = null as T
    }

    @Test
    fun parse() = runBlocking {
        val yandexAfishaCityParser = YandexAfishaCityParser(jacksonObjectMapper())
        val parse = yandexAfishaCityParser.parse()
        assertTrue(parse.isNotEmpty())
    }
}