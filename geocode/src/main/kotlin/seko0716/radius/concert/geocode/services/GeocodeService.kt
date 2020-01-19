package seko0716.radius.concert.geocode.services

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seko0716.radius.concert.geocode.domains.Geocode


interface GeocodeService {
    fun searchGeocodes(name: String): Flux<Geocode>
    fun getGeocodeById(name: String): Mono<Geocode>
    fun getAllGeocodes(): Flux<Geocode>
}