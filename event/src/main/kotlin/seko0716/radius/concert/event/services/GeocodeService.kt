package seko0716.radius.concert.event.services

import org.springframework.data.geo.Point


interface GeocodeService {

    fun initGeocodes()

    suspend fun getGeocode(name: String): Point
}