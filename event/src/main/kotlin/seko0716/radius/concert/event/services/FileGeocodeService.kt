package seko0716.radius.concert.event.services

import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.geo.Point
import org.springframework.stereotype.Component
import java.io.File
import javax.annotation.PostConstruct

@Component
class FileGeocodeService @Autowired constructor(
    @Value("\${spring.application.geocode.file.path}") private val path2Geocodes: String
) : GeocodeService {
    companion object {
        @JvmField
        val geocodes: MutableMap<String, Point> = mutableMapOf()
    }

    @PostConstruct
    override fun initGeocodes() {
        Jsoup.parse(File(path2Geocodes).readText())
            .select("row").map {
                it.select("Город").text() to Point(
                    it.select("Широта").text().toDouble(),
                    it.select("Долгота").text().toDouble()
                )
            }.toMap(geocodes)
    }

    override suspend fun getGeocode(name: String): Point {
        return geocodes[name] ?: Point(Double.NaN, Double.NaN)
    }
}