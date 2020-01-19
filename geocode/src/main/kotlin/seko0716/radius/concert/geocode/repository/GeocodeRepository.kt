package seko0716.radius.concert.geocode.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seko0716.radius.concert.geocode.domains.Geocode

@Repository
class GeocodeRepository @Autowired constructor(
    private val geocodeStorage: Map<String, Geocode>
) {
    private val values = Flux.fromIterable(geocodeStorage.values)

    fun getGeocodesByTemplate(template: String): Flux<Geocode> {
        return values.filter { it.nameForSearch.contains(template) }
    }

    fun findById(name: String): Mono<Geocode> {
        return Mono.justOrEmpty(geocodeStorage[name]).defaultIfEmpty(Geocode.EMPTY_GEOCODE)
    }

    fun getAllGeocodes(): Flux<Geocode> {
        return values
    }
}