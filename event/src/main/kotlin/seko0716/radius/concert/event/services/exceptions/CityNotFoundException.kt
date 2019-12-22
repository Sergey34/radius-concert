package seko0716.radius.concert.event.services.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "City not found")
class CityNotFoundException(city: String) : RuntimeException("City '${city}'not found")