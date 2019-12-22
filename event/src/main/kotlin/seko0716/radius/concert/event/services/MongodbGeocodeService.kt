package seko0716.radius.concert.event.services

import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.geo.Point
import org.springframework.stereotype.Service
import seko0716.radius.concert.event.domains.Geocode
import seko0716.radius.concert.event.repository.GeocodeRepository

@Service
class MongodbGeocodeService @Autowired constructor(
    private val geocodeRepository: GeocodeRepository
) : GeocodeService {

    @Cacheable(cacheNames = ["point"])
    override suspend fun getGeocode(name: String): Point {
        return geocodeRepository.find(name.toLowerCase())?.point
            ?: Point(Double.NaN, Double.NaN)
    }

    override suspend fun addGeocodes(geocodes: List<Geocode>): Flow<Geocode> {
        return geocodeRepository.saveAll(geocodes)
    }

    @Cacheable(cacheNames = ["geocodes"])
    override suspend fun searchGeocodes(name: String): Flow<Geocode> {
        return geocodeRepository.getGeocodesByTemplate(name.toLowerCase())
    }
}