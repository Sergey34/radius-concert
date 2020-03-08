package seko0716.radius.concert.blog.domain

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import seko0716.radius.concert.security.domains.User
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Document
data class Blog(
    @Id
    @JsonSerialize(using = ToStringSerializer::class)
    val id: ObjectId = ObjectId.get(),
    val title: String = "",
    val mainImege: String = "",
    val preview: String = "",
    val content: String = "",
    val event: String? = null,
    var author: User? = null,
    var tags: List<String> = listOf(),
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @field:JsonSerialize(using = LocalDateTimeSerializer::class)
    @field:JsonDeserialize(using = LocalDateTimeDeserializer::class)
    val createdWhen: LocalDateTime = ZonedDateTime.now(Clock.systemUTC()).toLocalDateTime()
)