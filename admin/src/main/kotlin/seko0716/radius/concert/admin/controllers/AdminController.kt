package seko0716.radius.concert.admin.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import seko0716.radius.concert.admin.services.ParserService
import seko0716.radius.concert.admin.services.StatusUpdate
import seko0716.radius.concert.event.domains.Event

@RestController
@RequestMapping("/api/admin")
class AdminController @Autowired constructor(
    private val parserService: ParserService
) {

    @GetMapping("/update")
    suspend fun updateData(): List<Event> {
        return parserService.updateData()
    }

    @GetMapping("/update/status")
    suspend fun status(): StatusUpdate? {
        return parserService.status()
    }
}