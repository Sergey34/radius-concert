package seko0716.radius.concert.notification.domains

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Clock
import java.time.ZonedDateTime

@Document
@JsonIgnoreProperties(ignoreUnknown = true)
data class Message(
    val eventId: String,
    @Id
    val id: ObjectId = ObjectId.get(),
    var author: String = "Гость",
    val content: String,
    val recipient: String? = null,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @field:JsonSerialize(using = ZonedDateTimeSerializer::class)
    val createdWhen: ZonedDateTime = ZonedDateTime.now(Clock.systemUTC())
)