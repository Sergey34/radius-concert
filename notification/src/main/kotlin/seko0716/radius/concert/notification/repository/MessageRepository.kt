package seko0716.radius.concert.notification.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import seko0716.radius.concert.notification.domains.Message

interface MessageRepository : ReactiveMongoRepository<Message, ObjectId> {
}