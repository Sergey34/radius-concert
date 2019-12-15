package seko0716.radius.concert.event.repository


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.geo.Circle
import org.springframework.data.geo.Distance
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import seko0716.radius.concert.event.domains.Coordinate
import seko0716.radius.concert.event.domains.Event


@Repository
class EventRepository @Autowired constructor(
    private val mongoTemplate: ReactiveMongoTemplate
) {
    fun saveAll(events: Collection<Event>): Flow<Event> {
        return mongoTemplate.insertAll(events).asFlow()
    }

    fun getEvents(coordinate: Coordinate, distance: Distance): Flow<Event> {
        return mongoTemplate.find<Event>(
            Query.query(
                Criteria.where("city.position").withinSphere(Circle(coordinate, distance))
            )
        ).asFlow()
    }

}

