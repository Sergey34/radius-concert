package seko0716.radius.concert.event.services

import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import seko0716.radius.concert.event.domains.Coordinate
import java.io.File
import javax.annotation.PostConstruct

@Component
class FileGeocodeService @Autowired constructor(
    @Value("\${spring.application.geocode.file.path}") val path2Geocodes: String,
    val geocodes: MutableMap<String, Coordinate> = mutableMapOf()
) : GeocodeService {

    @PostConstruct
    override suspend fun initGeocodes() {
        Jsoup.parse(File(path2Geocodes).readText())
            .select("row").map {
                it.select("Город").text() to Coordinate(
                    latt = it.select("Широта").text().toDouble(),
                    longt = it.select("Долгота").text().toDouble()
                )
            }.toMap(geocodes)
    }

    override suspend fun getGeocode(name: String): Coordinate {
        return geocodes[name] ?: Coordinate(Double.NaN, Double.NaN)
    }
}