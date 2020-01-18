package seko0716.radius.concert.event.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seko0716.radius.concert.event.domains.Geocode
import seko0716.radius.concert.event.repository.GeocodeRepository

@Service
class MongodbGeocodeService @Autowired constructor(
    private val geocodeRepository: GeocodeRepository
) : GeocodeService {

    @Cacheable(cacheNames = ["geocode"])
    override fun getGeocodeById(name: String): Mono<Geocode> {
        return geocodeRepository.findById(name.toLowerCase())
    }

    override fun addGeocodes(geocodes: List<Geocode>): Flux<Geocode> {
        return geocodeRepository.saveAll(geocodes)
    }

    @Cacheable(cacheNames = ["geocodes"])
    override fun searchGeocodes(name: String): Flux<Geocode> {
        return geocodeRepository.getGeocodesByTemplate(name.toLowerCase())
    }
}