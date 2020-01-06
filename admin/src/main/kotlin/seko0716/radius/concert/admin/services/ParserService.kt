package seko0716.radius.concert.admin.services

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import seko0716.radius.concert.admin.services.parsers.CityParser
import seko0716.radius.concert.admin.services.parsers.EventParser
import seko0716.radius.concert.event.domains.Event
import seko0716.radius.concert.event.repository.EventRepository

@Service
class ParserService @Autowired constructor(
    val cityParser: CityParser,
    val eventParser: EventParser,
    val eventRepository: EventRepository
) {

    suspend fun updateData(): List<Event> = coroutineScope {
        val events = cityParser.parse()
            .map {
                async {
                    eventRepository.saveAllSync(eventParser.parse(it))
                }
            }.flatMap { it.await() }
        events
    }

    @Scheduled(cron = "\${spring.application.events.dataset.refresh}")
    fun updateScheduleData() = runBlocking {
        val updateData = updateData()
    }
}