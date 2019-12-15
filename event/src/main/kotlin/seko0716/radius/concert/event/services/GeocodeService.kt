package seko0716.radius.concert.event.services

import seko0716.radius.concert.event.domains.Coordinate

interface GeocodeService {

    suspend fun initGeocodes()

    suspend fun getGeocode(name: String): Coordinate
}