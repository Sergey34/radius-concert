package seko0716.radius.concert.event.services.parsers.yandex.afisha

import seko0716.radius.concert.event.domains.City

internal data class YandexCityResponse(
    val data: List<City>
)