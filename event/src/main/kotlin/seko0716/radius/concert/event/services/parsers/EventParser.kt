package seko0716.radius.concert.event.services.parsers

import seko0716.radius.concert.event.domains.City
import seko0716.radius.concert.event.domains.Event

interface EventParser {
    suspend fun parse(city: City): List<Event>
    fun type(): String
}