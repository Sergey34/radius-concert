package seko0716.radius.concert.event.services.parsers.kassir

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.nodes.Element
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import seko0716.radius.concert.event.config.addToCollection
import seko0716.radius.concert.event.config.attempt
import seko0716.radius.concert.event.domains.City
import seko0716.radius.concert.event.domains.Event
import seko0716.radius.concert.event.domains.Event.EventInfo
import seko0716.radius.concert.event.domains.Event.ScheduleInfo
import seko0716.radius.concert.event.services.parsers.EventParser
import seko0716.radius.concert.event.services.parsers.kassir.KassirCityParser.Companion.ATTR_HREF
import seko0716.radius.concert.event.services.parsers.kassir.KassirCityParser.Companion.CSS_QUERY_CITY
import seko0716.radius.concert.event.services.parsers.kassir.KassirPage.Companion.INVALID_KASSIR_PAGE
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class KassirEventsEventParser @Autowired constructor(
    private val mapper: ObjectMapper
) : EventParser {
    companion object {
        const val suffix: String = "/kassir/frontpage/page?p="
        const val TYPE: String = "Kassir"
        const val JSON_DATA: String = "a.btn.btn-primary.btn-lg.js-ec-click-product"
        const val JSON_DATA_ITEM: String = "data-ec-item"
        const val TITLE: String = "div.title"
        const val PLACE: String = "div.place"
        const val CAPTION: String = "div.caption"
        @JvmField
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }

    override suspend fun parse(city: City): List<Event> {
        println("$TYPE $city")
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
        val place = event.select(PLACE).text()
        Event(
            city = city,
            event = EventInfo(
                argument = null,
                id = url,
                image = EventInfo.Image(
                    microdata = EventInfo.Image.Img(data["image"] as String? ?: "")
                ),
                contentRating = "",
                title = data["name"] as String,
                url = url,
                tickets = listOf(
                    EventInfo.Ticket(
                        EventInfo.Ticket.Price(data["maxPrice"] as Int? ?: 0, data["minPrice"] as Int? ?: 0)
                    )
                ),
                type = EventInfo.Type(data["category"] as String, data["category"] as String)

            ),
            scheduleInfo = ScheduleInfo(
                dateStarted = parseDate(data, "start_min").toLocalDate(),
                dateEnd = parseDate(data, "end_max").toLocalDate(),
                regularity = ScheduleInfo.Regularity(parseDate(data, "start_min")),
                oneOfPlaces = ScheduleInfo.OneOfPlaces(
                    address = place,
                    title = place,
                    coordinates = ScheduleInfo.OneOfPlaces.Coordinates(Double.NaN, Double.NaN)
                )
            )
        )
    }) { e -> e.printStackTrace(); null }

    private suspend fun getPage(url: String): KassirPage {
        return attempt({
            withContext(Dispatchers.IO) {
                mapper.readValue<KassirPage>(URL(url))
            }
        }) { e -> e.printStackTrace(); return INVALID_KASSIR_PAGE }
    }

    override fun type() = TYPE

    private suspend fun parseDate(data: Map<String, Any>, type: String): LocalDateTime {
        return when (val date = data["date"]) {
            is String -> LocalDateTime.parse(date, dateFormat)
            is Map<*, *> -> LocalDateTime.parse(date[type] as String, dateFormat)
            else -> throw IllegalStateException("Can not parse date")
        }
    }
}