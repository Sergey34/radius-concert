package seko0716.radius.concert.event.services.parsers.kassir

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import seko0716.radius.concert.event.domains.City
import seko0716.radius.concert.event.services.FileGeocodeService
import seko0716.radius.concert.event.services.parsers.CityParser


@Component
class KassirCityParser @Autowired constructor(
    val geocodeService: FileGeocodeService
) : CityParser {
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
                .map { City("Kassir", it.attr("href"), it.text(), geocodeService.getGeocode(it.text())) }
        }
    }
}