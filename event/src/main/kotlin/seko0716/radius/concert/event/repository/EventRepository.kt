package seko0716.radius.concert.event.repository


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.geo.Circle
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.findAll
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seko0716.radius.concert.event.domains.Event
import java.time.LocalDateTime


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
        return mongoTemplate.find(
            Query().limit(count).with(
                Sort.by(
                    Sort.Direction.ASC,
                    "scheduleInfo.dateEnd"
                )
            )
        )
    }

    fun saveAllSync(parse: List<Event>): MutableList<Event> {
        return eventMongoRepository.saveAll(parse)
    }

    fun getEvents(
        point: Point,
        distance: Distance,
        genre: String?,
        start: LocalDateTime?,
        end: LocalDateTime?
    ): Flux<Event> {
        val criteria = mutableListOf(Criteria.where("city.position").withinSphere(Circle(point, distance)))
        genre?.run {
            if (genre != "all") {
                criteria.add(Criteria.where("event.genre").elemMatch(Criteria().`in`(this)))
            }
        }
        if (start != null && end != null) {
            criteria.add(
                Criteria().orOperator(
                    Criteria.where("scheduleInfo.regularity.singleShowtime").gte(start).lte(end)
                )
            )
        }

        return mongoTemplate.find(
            Query.query(Criteria().andOperator(*criteria.toTypedArray())).with(
                Sort.by(
                    Sort.Direction.ASC,
                    "scheduleInfo.dateEnd"
                )
            )
        )
    }

    fun findById(id: String): Mono<Event> {
        return mongoTemplate.findById(id)
    }

    fun getEventsByCity(city: String): Flux<Event> {
        return mongoTemplate.find(
            Query.query(Criteria.where("city.name").`is`(city)).with(
                Sort.by(
                    Sort.Direction.ASC,
                    "scheduleInfo.dateStarted"
                )
            )
        )
    }

    fun getEventsByTitle(title: String): Flux<Event> {
        return mongoTemplate.find(
            Query.query(Criteria.where("event.title").regex(title, "i")).with(
                Sort.by(
                    Sort.Direction.ASC,
                    "scheduleInfo.dateStarted"
                )
            )
        )
    }
}
