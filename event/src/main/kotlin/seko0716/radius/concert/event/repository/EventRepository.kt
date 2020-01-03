package seko0716.radius.concert.event.repository


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.geo.Circle
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.findAll
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import seko0716.radius.concert.event.domains.Event
import java.time.LocalDate


@Repository
class EventRepository @Autowired constructor(
    private val mongoTemplate: ReactiveMongoTemplate,
    private val eventReactiveMongoRepository: EventReactiveMongoRepository,
    private val eventMongoRepository: EventMongoRepository
) {
    fun saveAll(events: Collection<Event>): Flow<Event> {
        return eventReactiveMongoRepository.saveAll(events).asFlow()
    }

    fun getAllEvents(): Flow<Event> {
        return mongoTemplate.findAll<Event>().asFlow()
    }

    fun getEvents(count: Int): Flow<Event> {
        return mongoTemplate.find<Event>(Query().limit(count)).asFlow()
    }

    fun saveAllSync(parse: List<Event>): Flow<Event> {
        return eventMongoRepository.saveAll(parse).asFlow()
    }

    fun getEvents(point: Point, distance: Distance, genre: String?, start: LocalDate?, end: LocalDate?): Flow<Event> {
        val criteria = mutableListOf(Criteria.where("city.position").withinSphere(Circle(point, distance)))
        genre?.run {
            if (genre != "all") {
                criteria.add(Criteria.where("event.genre").elemMatch(Criteria().`in`(this)))
            }
        }
        if (start != null && end != null) {
            criteria.add(
                Criteria().orOperator(
                    Criteria.where("scheduleInfo.dateStarted").gte(start).lte(end),
                    Criteria.where("scheduleInfo.dateEnd").gte(start).lte(end)
                )
            )
        }

        return mongoTemplate.find<Event>(Query.query(Criteria().andOperator(*criteria.toTypedArray()))).asFlow()
    }
}

@Repository
interface EventReactiveMongoRepository : ReactiveMongoRepository<Event, String> {
}

@Repository
interface EventMongoRepository : MongoRepository<Event, String> {
}
