package seko0716.radius.concert.event.repository


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Repository
import seko0716.radius.concert.event.domains.Event


@Repository
class EventRepository @Autowired constructor(
    private val mongoTemplate: ReactiveMongoTemplate
) {
    fun saveAll(events: Collection<Event>): Flow<Event> {
        return mongoTemplate.insertAll(events).asFlow()
    }

}

