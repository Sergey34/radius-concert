package seko0716.radius.concert.event.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import seko0716.radius.concert.event.domains.Event

@Repository
interface EventRepository : ReactiveMongoRepository<Event, ObjectId> {
}