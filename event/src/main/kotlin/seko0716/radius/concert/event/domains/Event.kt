package seko0716.radius.concert.event.domains

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.time.LocalDateTime

@Document
@JsonIgnoreProperties(ignoreUnknown = true)
data class Event(
    val event: EventInfo,
    val scheduleInfo: ScheduleInfo,
    var city: City = City("", "", ""),
    @Id
    val id: String = event.id
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class EventInfo(
        val argument: String?, // Елена Яковлева в драме об актёрском соперничестве
        val contentRating: String, // 16+
        val id: String, // 5bc61daf9f1dc9953f2881b0
        val image: Image?,
        var tickets: List<Ticket>,
        val title: String, // Вражда
        val type: Type,
        val tags: List<Tag>?,
        var url: String,
        val genre: List<String> = tags?.filter { it.type == "genre" }?.map { it.code } ?: listOf()
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Image(
            val microdata: Img
        ) {
            @JsonIgnoreProperties(ignoreUnknown = true)
            data class Img(
                val url: String
            )

            @JsonIgnoreProperties(ignoreUnknown = true)
            data class Source(
                val title: String?, // spektaklvrazhda.ru
                val url: Any? // null
            )
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Ticket(
            val price: Price?
        ) {
            @JsonIgnoreProperties(ignoreUnknown = true)
            data class Price(
                val max: Int, // 500000
                val min: Int, // 80000
                val currency: String = "rub" // rub
            )
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Type(
            val code: String, // theatre
            val name: String // Театр
        )

        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Tag(
            val code: String,
            val name: String,
            val type: String
        )
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ScheduleInfo(
        @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @field:JsonSerialize(using = LocalDateSerializer::class)
        @field:JsonDeserialize(using = LocalDateDeserializer::class)
        val dateStarted: LocalDate, // 2020-02-11
        @field:JsonDeserialize(using = LocalDateDeserializer::class)
        @field:JsonSerialize(using = LocalDateSerializer::class)
        @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        val dateEnd: LocalDate, // 2020-02-11
        val oneOfPlaces: OneOfPlaces?,
        val regularity: Regularity
    ) {

        @JsonIgnoreProperties(ignoreUnknown = true)
        data class OneOfPlaces(
            val address: String?, // ул. Рабочая, 116
            val coordinates: Coordinates?,
            val title: String? // Драматический театр им. Слонова
        ) {
            @JsonIgnoreProperties(ignoreUnknown = true)
            data class Coordinates(
                val latitude: Double, // 51.534083
                val longitude: Double // 46.00189@field:JsonSerialize(using = LocalDateSerializer::class)
            )
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Regularity(
            @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
            @field:JsonSerialize(using = LocalDateTimeSerializer::class)
            @field:JsonDeserialize(using = LocalDateTimeDeserializer::class)
            val singleShowtime: LocalDateTime?
        )
    }
}