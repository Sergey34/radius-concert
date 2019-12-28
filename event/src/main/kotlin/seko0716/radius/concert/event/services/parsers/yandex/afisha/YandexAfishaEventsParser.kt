package seko0716.radius.concert.event.services.parsers.yandex.afisha

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.context.annotation.Primary
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.stereotype.Component
import seko0716.radius.concert.event.config.addToCollection
import seko0716.radius.concert.event.config.attempt
import seko0716.radius.concert.event.domains.City
import seko0716.radius.concert.event.domains.Event
import seko0716.radius.concert.event.services.parsers.EventParser
import java.net.URL

//https://afisha.yandex.ru/api/events/actual?limit=20&offset=0&hasMixed=0&tag=concert&city=saratov
@Primary
@Component
class YandexAfishaEventsParser constructor(
    private val mapper: ObjectMapper
) : EventParser {
    companion object {
        const val URL: String = "https://afisha.yandex.ru/api/events/actual?limit=20&tag=concert&offset="
        const val suffix: String = "&hasMixed=0&city="
        const val TYPE: String = "Yandex Afisha"
    }

    override suspend fun parse(city: City): List<Event> {
        println("$TYPE $city start")
        var offset = 0
        var total: Int
        val result: MutableList<Event> = mutableListOf()
        do {
            val url = "$URL$offset$suffix${city.id}"
            val (paging, valid, data) = getPage(url)
            if (valid) {
                data
                    .filter { it.scheduleInfo.oneOfPlaces?.coordinates != null }
                    .map {
                        val (latitude, longitude) = it.scheduleInfo.oneOfPlaces!!.coordinates!!
                        val position = GeoJsonPoint(latitude, longitude)
                        it.city = City(city.type, city.url, city.name, position, city.id)
                        it.event.url = "https://afisha.yandex.ru${it.event.url}"
                        it.event.tickets = it.event.tickets
                            .filter { ticket -> ticket.price != null }
                            .map { ticket ->
                                val max = ticket.price!!.max / 100
                                val min = ticket.price.min / 100
                                Event.EventInfo.Ticket(Event.EventInfo.Ticket.Price(max, min))
                            }
                        it
                    }
                    .addToCollection(result)
            }
            offset += paging.limit
            total = paging.total
        } while (offset <= total && total != 0)
        println("$TYPE $city done $result")
        return result
    }

    override fun type() = TYPE

    private suspend fun getPage(url: String) = attempt({
        withContext(Dispatchers.IO) {
            mapper.readValue<YandexPage>(URL(url))
        }
    }) { e -> e.printStackTrace(); return YandexPage.INVALID_YANDEX_PAGE }
}