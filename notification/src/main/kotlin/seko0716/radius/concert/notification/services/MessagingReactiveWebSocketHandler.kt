package seko0716.radius.concert.notification.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.security.core.context.SecurityContext
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.UnicastProcessor
import seko0716.radius.concert.notification.domains.Message
import seko0716.radius.concert.notification.repository.MessageRepository
import seko0716.radius.concert.security.domains.User


@Component("messagingReactiveWebSocketHandler")
class MessagingReactiveWebSocketHandler constructor(
    private val objectMapper: ObjectMapper,
    private val eventPublisher: UnicastProcessor<Message>,
    private val messageRepository: MessageRepository,
    events: Flux<Message>
) : WebSocketHandler {
    private val outputEvents: Flux<String> = Flux.from(events).map { objectMapper.writeValueAsString(it) }

    fun toMessage(json: String): Mono<Message> {
        return Mono.subscriberContext()
            .map {
                val message = objectMapper.readValue<Message>(json)
                if (it.hasKey(SecurityContext::class.java)) {
                    val sc = it.get(SecurityContext::class.java)
                    message.author = (sc.authentication?.principal as User?)?.login ?: "Гость"
                }
                message
            }
    }

    override fun handle(session: WebSocketSession): Mono<Void> {
        val subscriber = WebSocketMessageSubscriber(eventPublisher)
        return session.receive()
            .flatMap { obj: WebSocketMessage -> toMessage(obj.payloadAsText) }
            .doOnNext { event -> subscriber.onNext(event) }
            .doOnError { error: Throwable -> subscriber.onError(error) }
            .doOnComplete { subscriber.onComplete() }
            .doOnNext { messageRepository.save(it) }
            .zipWith(session.send(outputEvents.map { payload: String ->
                session.textMessage(
                    payload
                )
            }))
            .then()
    }
}