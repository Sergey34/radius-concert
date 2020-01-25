package seko0716.radius.concert.admin.services

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer
import java.time.ZonedDateTime

data class StatusUpdate(
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @field:JsonSerialize(using = ZonedDateTimeSerializer::class)
    val start: ZonedDateTime,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @field:JsonSerialize(using = ZonedDateTimeSerializer::class)
    var end: ZonedDateTime? = null,
    var count: Int = 0
)
