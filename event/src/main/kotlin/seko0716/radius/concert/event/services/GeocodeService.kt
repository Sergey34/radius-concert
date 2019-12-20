package seko0716.radius.concert.event.services

import org.springframework.data.geo.Point
import seko0716.radius.concert.event.controllers.GeocodeInfo


interface GeocodeService {

    fun initGeocodes()

    suspend fun getGeocode(name: String): Point
    suspend fun addGeocodes(geocodesInfo: List<GeocodeInfo>)
}