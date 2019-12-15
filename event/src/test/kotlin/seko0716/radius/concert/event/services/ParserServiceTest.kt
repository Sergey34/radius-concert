package seko0716.radius.concert.event.services

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test
import seko0716.radius.concert.event.services.parsers.kassir.KassirCityParser

internal class ParserServiceTest {
    //    2104816516 async
    //    5237680992
    @Test
    fun updateData() = runBlocking {
        val start = System.nanoTime()
        val flatMap = listOf<Int>(1, 2, 3, 4, 5, 6, 7, 8)
            .map { async { foo(it) } }
            .flatMap { it.await() }
            .map { async { foo(it) } }
            .flatMap { it.await() }
        val stop = System.nanoTime()
        print(stop - start)
    }

    //    32039196658 sync
    //    40521038925
    @Test
    fun updateDataSync() = runBlocking {
        val start = System.nanoTime()
        val flatMap = listOf<Int>(1, 2, 3, 4, 5, 6, 7, 8)
            .flatMap { foo(it) }
            .flatMap { foo(it) }
        val stop = System.nanoTime()
        print(stop - start)
    }


    private suspend fun foo(it: Int): List<Int> {
//        return withContext(Dispatchers.IO) {
        delay(1000)
        Jsoup.connect(KassirCityParser.URL).get()
        return listOf(it, it * 2, it * 3)
//        }
    }
}