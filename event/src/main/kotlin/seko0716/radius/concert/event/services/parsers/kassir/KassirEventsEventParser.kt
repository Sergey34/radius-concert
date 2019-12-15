package seko0716.radius.concert.event.services.parsers.kassir

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import seko0716.radius.concert.event.config.addToCollection
import seko0716.radius.concert.event.config.attempt
import seko0716.radius.concert.event.domains.City
import seko0716.radius.concert.event.domains.Event
import seko0716.radius.concert.event.services.parsers.EventParser
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class KassirEventsEventParser : EventParser {
    companion object {
        const val suffix: String = "/kassir/frontpage/page?p="
        const val type: String = "Kassir"
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
            val pageData = attempt({
                withContext(Dispatchers.IO) {
                    mapper.readValue<Map<String, Any>>(URL("${city.url}${suffix}${page}"))
                }
            }) { return result }

            cnt = pageData["cnt"] as Int? ?: 0
            val html = attempt({
                Jsoup.parse(pageData["html"] as String?)
            }) { null } ?: continue

            html.run {
                select("div.caption").mapNotNull {
                    attempt({
                        val content = it.select("a.btn.btn-primary.btn-lg.js-ec-click-product").attr("data-ec-item")
                        val data = mapper.readValue<Map<String, Any>>(content)
                        val url = it.select("div.title").select("a").attr("href")
                        Event(
                            city = city,
                            url = url,
                            id = url,
                            name = data["name"] as String,
                            category = data["category"] as String,
                            categoryId = data["category_id"].toString(),
                            image = data["image"] as String,
                            maxPrice = data["maxPrice"] as Int? ?: 0,
                            minPrice = data["minPrice"] as Int? ?: 0,
                            thirdPartyId = data["id"].toString(),
                            endDate = parseDate(data, "end_max"),
                            startDate = parseDate(data, "start_min")
                        )
                    }) { null }
                }
            }.addToCollection(result)
            page++
        } while (page < cnt)
        return result
    }

    override fun type() = type

    private fun parseDate(data: Map<String, Any>, type: String): LocalDateTime {
        return when (val date = data["date"]) {
            is String -> LocalDateTime.parse(date, dateFormat)
            is Map<*, *> -> LocalDateTime.parse(date[type] as String, dateFormat)
            else -> throw IllegalStateException("Can not parse date")
        }
    }
}


