package seko0716.radius.concert.event.services.parsers

import seko0716.radius.concert.event.domains.Event

interface Parser {
    fun parse(url: String): Event
}