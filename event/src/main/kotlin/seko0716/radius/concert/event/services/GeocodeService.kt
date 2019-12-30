package seko0716.radius.concert.event.services

import kotlinx.coroutines.flow.Flow
import seko0716.radius.concert.event.domains.Geocode


interface GeocodeService {
    suspend fun getGeocode(name: String): Geocode

    suspend fun addGeocodes(geocodes: List<Geocode>): Flow<Geocode>

    suspend fun searchGeocodes(name: String): Flow<Geocode>
    suspend fun getGeocodeById(name: String): Geocode
}