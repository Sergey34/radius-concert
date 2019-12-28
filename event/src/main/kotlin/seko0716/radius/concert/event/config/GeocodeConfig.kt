package seko0716.radius.concert.event.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.MongoTemplate
import seko0716.radius.concert.event.domains.Geocode
import seko0716.radius.concert.event.services.GeocodeService
import java.io.File
import javax.annotation.PostConstruct


@Configuration
class GeocodeConfig @Autowired constructor(
    private val mongoTemplate: MongoTemplate,
    private val mongodbGeocodeService: GeocodeService
) {
    companion object {
        @JvmField
        val mapper = jacksonObjectMapper()
    }

    @PostConstruct
    fun importGeocodes() {
        println("load geocodes")
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