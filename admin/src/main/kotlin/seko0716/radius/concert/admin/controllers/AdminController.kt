package seko0716.radius.concert.admin.controllers

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import seko0716.radius.concert.admin.services.ParserService
import seko0716.radius.concert.event.domains.Event

@RestController
@RequestMapping("/api")
class AdminController @Autowired constructor(
    private val parserService: ParserService
) {
    @FlowPreview
    @GetMapping("/update")
    suspend fun updateData(): Flow<Event> {
        return parserService.updateData()
    }
}