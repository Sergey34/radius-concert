package seko0716.radius.concert.event.domains

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Event(
    @Id
    val id: ObjectId,
    val name: String,
    val thirdPartyId: String,
    val category: String,
    val categoryId: String,
    val image: String,
    val minPrice: Int,
    val maxPrice: Int,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val city: City,
    val url: String
)