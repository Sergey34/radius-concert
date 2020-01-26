package seko0716.radius.concert.geocode.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seko0716.radius.concert.geocode.domains.Geocode

@Repository
class GeocodeRepository @Autowired constructor(
    private val geocodeStorage: Map<String, Geocode>,
    private val geocodeGroups: Map<String, List<Geocode>>
) {

    fun getGeocodesByTemplate(template: String): Flux<Geocode> {
        return Flux.fromIterable(geocodeGroups.filter { it.key.contains(template) }.flatMap { it.value })
    }

    fun findById(name: String): Mono<Geocode> {
        val geocode = geocodeGroups[name]
            .takeIf { it?.size == 1 }
            ?.last() ?: geocodeStorage[name]

        return Mono.justOrEmpty(geocode).defaultIfEmpty(Geocode.EMPTY_GEOCODE)
    }
}