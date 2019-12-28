package seko0716.radius.concert.admin.services.parsers

import seko0716.radius.concert.event.domains.City

interface CityParser {
    suspend fun parse(): List<City>
}