package seko0716.radius.concert.event.services.parsers.kassir

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import seko0716.radius.concert.event.config.attempt
import seko0716.radius.concert.event.domains.City
import seko0716.radius.concert.event.services.GeocodeService
import seko0716.radius.concert.event.services.parsers.CityParser


@Component
class KassirCityParser @Autowired constructor(
    val geocodeService: GeocodeService
) : CityParser {
    companion object {
        const val URL: String = "https://www.kassir.ru/"
        const val TYPE = "Kassir"
        const val CSS_QUERY_CITIES = "ul.cities"
        const val CSS_QUERY_CITY = "a"
        const val ATTR_HREF = "href"
    }

    override suspend fun parse() = attempt({
        withContext(Dispatchers.IO) {
            Jsoup.connect(URL).get()
        }.run {
            select(CSS_QUERY_CITIES)
                .flatMap { it.select(CSS_QUERY_CITY) }
                .map {
                    val position = geocodeService.getGeocode("россия, ${it.text()}")
                    City(TYPE, it.attr(ATTR_HREF), it.text(), position)
                }
        }
    }) { e -> e.printStackTrace(); listOf() }
}