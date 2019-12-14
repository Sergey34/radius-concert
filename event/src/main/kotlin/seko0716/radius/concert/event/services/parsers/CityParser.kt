package seko0716.radius.concert.event.services.parsers

import seko0716.radius.concert.event.domains.City

interface CityParser {
    suspend fun parse(): List<City>
}