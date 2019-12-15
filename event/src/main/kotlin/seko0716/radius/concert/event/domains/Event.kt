package seko0716.radius.concert.event.domains

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Event(
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
    val url: String,
    @Id
    val id: String = "$thirdPartyId${city.name}${city.type}"
)