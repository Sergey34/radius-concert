package seko0716.radius.concert.event.domains

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Event(
    @Id
    val id: ObjectId
)