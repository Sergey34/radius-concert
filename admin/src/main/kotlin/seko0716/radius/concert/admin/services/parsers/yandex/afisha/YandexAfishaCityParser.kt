package seko0716.radius.concert.admin.services.parsers.yandex.afisha

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import seko0716.radius.concert.admin.services.parsers.CityParser
import seko0716.radius.concert.event.domains.City
import seko0716.radius.concert.shared.attempt
import java.net.URL

@Primary
@Component
class YandexAfishaCityParser @Autowired constructor(
    private val mapper: ObjectMapper
) : CityParser {
    companion object {
        const val URL: String = "https://afisha.yandex.ru/api/cities?city=moscow"
        const val TYPE = "Yandex Afisha"
    }

    override suspend fun parse() = attempt({
        println(TYPE)
        withContext(Dispatchers.IO) {
            mapper.readValue<YandexCityResponse>(URL(URL)).data
        }
            .map {
                println(it)
                City(
                    TYPE,
                    it.url,
                    it.name,
                    it.position,
                    it.id
                )
            }

    }) { e -> e.printStackTrace(); listOf() }
}