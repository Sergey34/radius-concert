package seko0716.radius.concert.event.repository


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
import reactor.core.publisher.Flux
import seko0716.radius.concert.event.domains.Event
import java.time.LocalDate


@Repository
class EventRepository @Autowired constructor(
    private val mongoTemplate: ReactiveMongoTemplate,
    private val eventReactiveMongoRepository: EventReactiveMongoRepository,
    private val eventMongoRepository: EventMongoRepository
) {
    fun saveAll(events: Collection<Event>): Flux<Event> {
        return eventReactiveMongoRepository.saveAll(events)
    }

    fun getAllEvents(): Flux<Event> {
        return mongoTemplate.findAll()
    }

    fun getEvents(count: Int): Flux<Event> {
        return mongoTemplate.find(Query().limit(count))
    }

    fun saveAllSync(parse: List<Event>): MutableList<Event> {
        return eventMongoRepository.saveAll(parse)
    }

    fun getEvents(point: Point, distance: Distance, genre: String?, start: LocalDate?, end: LocalDate?): Flux<Event> {
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

        return mongoTemplate.find<Event>(Query.query(Criteria().andOperator(*criteria.toTypedArray())))
    }
}

@Repository
interface EventReactiveMongoRepository : ReactiveMongoRepository<Event, String> {
}

@Repository
interface EventMongoRepository : MongoRepository<Event, String> {
}
