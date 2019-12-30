package seko0716.radius.concert.admin.services.parsers.kassir

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.stereotype.Component
import seko0716.radius.concert.admin.services.parsers.CityParser
import seko0716.radius.concert.event.domains.City
import seko0716.radius.concert.event.services.GeocodeService
import seko0716.radius.concert.shared.attempt


@Component
class KassirCityParser @Autowired constructor(
    val geocodeService: GeocodeService
) : CityParser {
    companion object {
        @JvmField
        val logger: Logger = LoggerFactory.getLogger(KassirCityParser::class.java)
        const val URL: String = "https://www.kassir.ru/"
        const val TYPE = "Kassir"
        const val CSS_QUERY_CITIES = "ul.cities"
        const val CSS_QUERY_CITY = "a"
        const val ATTR_HREF = "href"
    }

    override suspend fun parse() = attempt({
        logger.debug("[{}] Start parsing", TYPE)
        withContext(Dispatchers.IO) {
            Jsoup.connect(URL).get()
        }.run {
            select(CSS_QUERY_CITIES)
                .flatMap { it.select(CSS_QUERY_CITY) }
                .map {
                    val position = geocodeService.getGeocode(it.text().toLowerCase()).point
                    City(
                        TYPE, it.attr(
                            ATTR_HREF
                        ), it.text(), GeoJsonPoint(position)
                    )
                }
        }
    }) { e -> logger.warn("[${KassirEventParser.TYPE}]", e); listOf() }
}