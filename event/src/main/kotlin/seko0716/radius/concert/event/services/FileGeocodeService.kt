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
        const val RAW: String = "row"
        const val CITY: String = "Город"
        const val X: String = "Широта"
        const val Y: String = "Долгота"
    }

    @PostConstruct
    override fun initGeocodes() {
        Jsoup.parse(File(path2Geocodes).readText())
            .select(RAW).map {
                it.select(CITY).text() to Point(
                    it.select(X).text().toDouble(),
                    it.select(Y).text().toDouble()
                )
            }.toMap(geocodes)
    }

    override suspend fun getGeocode(name: String): Point {
        return geocodes[name] ?: Point(Double.NaN, Double.NaN)
    }
}