package seko0716.radius.concert.event.services

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seko0716.radius.concert.event.domains.Geocode


interface GeocodeService {
    fun addGeocodes(geocodes: List<Geocode>): Flux<Geocode>
    fun searchGeocodes(name: String): Flux<Geocode>
    fun getGeocodeById(name: String): Mono<Geocode>
}