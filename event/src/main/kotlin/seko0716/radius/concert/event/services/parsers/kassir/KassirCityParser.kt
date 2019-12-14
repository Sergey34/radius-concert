package seko0716.radius.concert.event.services.parsers.kassir

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import seko0716.radius.concert.event.domains.City
import seko0716.radius.concert.event.domains.Coordinate
import seko0716.radius.concert.event.services.parsers.CityParser
import java.net.URL


@Component
class KassirCityParser : CityParser {
    companion object {
        const val URL: String = "https://www.kassir.ru/"
        @JvmField
        val mapper = jacksonObjectMapper()
    }

    override suspend fun parse(): List<City> {
        return withContext(Dispatchers.IO) {
            Jsoup.connect(URL).get()
        }.run {
            select("ul.cities")
                .flatMap { it.select("a") }
                .map { City("Kassir", it.attr("href"), it.text(), getGeocode(it.text())) }
        }
    }

    //todo rewrite to https://gist.github.com/nalgeon/5307af065ff0e3bc97927c832fabe26b
    private fun getGeocode(name: String): Any {
        return try {
            val readValue =
                mapper.readValue<Map<String, Any>>(URL("https://geocode.xyz/${name}?json=1&auth=535808461301567696423x4142"))
            Coordinate(
                readValue["longt"]?.toString()?.toDouble() ?: Double.NaN,
                readValue["latt"]?.toString()?.toDouble() ?: Double.NaN
            )
        } catch (e: Exception) {
            Coordinate(Double.NaN, Double.NaN)
        }
    }
}