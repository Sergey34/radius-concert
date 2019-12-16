package seko0716.radius.concert.event.services.parsers.kassir

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component
import seko0716.radius.concert.event.config.addToCollection
import seko0716.radius.concert.event.config.attempt
import seko0716.radius.concert.event.domains.City
import seko0716.radius.concert.event.domains.Event
import seko0716.radius.concert.event.services.parsers.EventParser
import seko0716.radius.concert.event.services.parsers.kassir.KassirCityParser.Companion.ATTR_HREF
import seko0716.radius.concert.event.services.parsers.kassir.KassirCityParser.Companion.CSS_QUERY_CITY
import seko0716.radius.concert.event.services.parsers.kassir.KassirPage.Companion.INVALID_KASSIR_PAGE
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class KassirEventsEventParser : EventParser {
    companion object {
        const val suffix: String = "/kassir/frontpage/page?p="
        const val type: String = "Kassir"
        const val JSON_DATA: String = "a.btn.btn-primary.btn-lg.js-ec-click-product"
        const val JSON_DATA_ITEM: String = "data-ec-item"
        const val TITLE: String = "div.title"
        const val CAPTION: String = "div.caption"
        @JvmField
        val mapper = jacksonObjectMapper()
        @JvmField
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }

    override suspend fun parse(city: City): List<Event> {
        var page = 0
        var cnt: Int
        val result: MutableList<Event> = mutableListOf()
        do {
            val pageData = getPage("${city.url}${suffix}${page}")
            cnt = pageData.cnt
            if (pageData.valid) {
                pageData.doc.run {
                    select(CAPTION)
                        .mapNotNull { event -> createEvent(event, city) }
                }.addToCollection(result)
            }
            page++
        } while (page < cnt)
        return result
    }

    private suspend fun createEvent(event: Element, city: City) = attempt({
        val content = event.select(JSON_DATA).attr(JSON_DATA_ITEM)
        val data = mapper.readValue<Map<String, Any>>(content)
        val url = event.select(TITLE).select(CSS_QUERY_CITY).attr(ATTR_HREF)
        Event(
            city = city,
            url = url,
            id = url,
            name = data["name"] as String,
            category = data["category"] as String,
            categoryId = data["category_id"].toString(),
            image = data["image"] as String? ?: "",
            maxPrice = data["maxPrice"] as Int? ?: 0,
            minPrice = data["minPrice"] as Int? ?: 0,
            thirdPartyId = data["id"].toString(),
            endDate = parseDate(data, "end_max"),
            startDate = parseDate(data, "start_min")
        )
    }) { e -> e.printStackTrace(); null }

    private suspend fun getPage(url: String): KassirPage {
        return attempt({
            withContext(Dispatchers.IO) {
                mapper.readValue<KassirPage>(URL(url))
            }
        }) { e -> e.printStackTrace(); return INVALID_KASSIR_PAGE }
    }

    override fun type() = type

    private suspend fun parseDate(data: Map<String, Any>, type: String): LocalDateTime {
        return when (val date = data["date"]) {
            is String -> LocalDateTime.parse(date, dateFormat)
            is Map<*, *> -> LocalDateTime.parse(date[type] as String, dateFormat)
            else -> throw IllegalStateException("Can not parse date")
        }
    }
}