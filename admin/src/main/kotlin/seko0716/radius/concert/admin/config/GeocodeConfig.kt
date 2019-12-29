package seko0716.radius.concert.admin.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.count
import seko0716.radius.concert.event.domains.Geocode
import java.io.File
import javax.annotation.PostConstruct

@Profile("load_geocode_config")
@Configuration
class GeocodeConfig @Autowired constructor(
    private val mongoTemplate: MongoTemplate,
    private val mapper: ObjectMapper
) {
    companion object {
        @JvmField
        val logger: Logger = LoggerFactory.getLogger(GeocodeConfig::class.java)
    }

    @PostConstruct
    fun importGeocodes() {
        if (mongoTemplate.count<Geocode>() != 0L) {
            return
        }
        logger.info("Load geocodes")
        val distinctBy = mapper.readValue<List<Pair<String, SerderPoint>>>(File("geocodes.json"))
            .map { Geocode(it.first.toLowerCase(), it.first, Point(it.second.y, it.second.x)) }
            .distinctBy { it.nameForSearch }
        distinctBy
            .let {
                mongoTemplate.insertAll(it)
            }
    }
}

data class SerderPoint(
    val x: Double,
    val y: Double
)