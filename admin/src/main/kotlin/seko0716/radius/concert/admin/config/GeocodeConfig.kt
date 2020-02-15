package seko0716.radius.concert.admin.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import seko0716.radius.concert.geocode.domains.Geocode
import java.io.File

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
    fun geocodeGroups(): Map<String, List<Geocode>> {
        return mapper.readValue(File("ttt.json"))
    }

    @Bean
    fun geocodeStorage(): Map<String, Geocode> {
        logger.info("Load geocodes")

        return geocodeGroups()
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