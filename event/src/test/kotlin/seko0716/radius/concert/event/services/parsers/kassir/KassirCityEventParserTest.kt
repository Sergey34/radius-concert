package seko0716.radius.concert.event.services.parsers.kassir

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class KassirCityEventParserTest {

    @Test
    fun parse() = runBlocking {
        val parse = KassirCityParser().parse()
        Assertions.assertTrue(parse.isNotEmpty())
    }
}