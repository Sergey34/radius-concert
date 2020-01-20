package seko0716.radius.concert.admin.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.geo.Point
import seko0716.radius.concert.geocode.domains.Geocode
import java.io.File

@Profile("load_geocode_config")
@Configuration
class GeocodeConfig
@Autowired constructor(
    private val mapper: ObjectMapper
) {
    companion object {
        @JvmField
        val logger: Logger = LoggerFactory.getLogger(GeocodeConfig::class.java)
    }

    @Bean
    fun geocodes(): Map<String, List<Geocode>> {
        return mapper.readValue<List<Pair<String, SerderPoint>>>(File("geocodes.json"))
            .asSequence()
            .filter { it.first.startsWith("Россия, ") }
            .map { g ->
                val name = g.first.removePrefix("Россия, ")
                Geocode(
                    name.toLowerCase().substring(
                        name.toLowerCase().lastIndexOf(',') + 1,
                        name.toLowerCase().length
                    ).trim(),
                    name,
                    Point(g.second.y, g.second.x)
                )
            }
            .distinctBy { it.name.toLowerCase() }
            .groupBy { it.nameForSearch }
    }

    @Bean
    fun geocodeStorage(): Map<String, Geocode> {
        logger.info("Load geocodes")

        return geocodes()
            .toMap()
            .flatMap {
                if (it.value.size == 1) {
                    it.value.map { g ->
                        g.name.toLowerCase() to g
                    }
                } else {
                    it.value.map { g ->
                        g.name.toLowerCase() to Geocode(
                            g.name.toLowerCase(),
                            g.name,
                            g.point
                        )
                    }
                }
            }.toMap()
    }
}

data class SerderPoint(
    val x: Double,
    val y: Double
)