package seko0716.radius.concert.event.services

import kotlinx.coroutines.flow.Flow
import org.springframework.data.geo.Point
import seko0716.radius.concert.event.domains.Geocode


interface GeocodeService {
    suspend fun getGeocode(name: String): Point

    suspend fun addGeocodes(geocodes: List<Geocode>): Flow<Geocode>

    suspend fun searchGeocodes(name: String): Flow<Geocode>
}