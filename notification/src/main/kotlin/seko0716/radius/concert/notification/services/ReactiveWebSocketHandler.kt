package seko0716.radius.concert.notification.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.UnicastProcessor
import seko0716.radius.concert.security.domains.User
import java.util.*


@Component("reactiveWebSocketHandler")
class ReactiveWebSocketHandler constructor(
    private val objectMapper: ObjectMapper,
    private val eventPublisher: UnicastProcessor<Map<String, String>>,
    events: Flux<Map<String, String>>
) : WebSocketHandler {
    private val outputEvents: Flux<String> = Flux.from(events).map { json.writeValueAsString(it) }

    fun toMessage(json: String): Mono<MutableMap<String, String>> {
        return ReactiveSecurityContextHolder.getContext()
            .map { it.authentication }
            .map { a ->
                objectMapper.readValue<MutableMap<String, String>>(json)
                    .apply { put("userName", (a.principal as User).login) }
            }
    }

    override fun handle(session: WebSocketSession): Mono<Void> {
        val subscriber = WebSocketMessageSubscriber(eventPublisher)
        return session.receive()
            .map { obj: WebSocketMessage -> obj.payloadAsText }
            .flatMap { toMessage(it) }
            .doOnNext { event -> subscriber.onNext(event) }
            .doOnError { error: Throwable -> subscriber.onError(error) }
            .doOnComplete { subscriber.onComplete() }
            .zipWith(session.send(outputEvents.map { payload: String ->
                session.textMessage(
                    payload
                )
            }))
            .then()
    }

    companion object {
        private val json = ObjectMapper()
    }
}

class WebSocketMessageSubscriber constructor(
    private val eventPublisher: UnicastProcessor<Map<String, String>>,
    private var lastReceivedEvent: Optional<Map<String, String>> = Optional.empty()
) {
    fun onNext(event: Map<String, String>) {
        lastReceivedEvent = Optional.of(event)
        eventPublisher.onNext(event)
    }

    fun onError(error: Throwable) { //TODO log error
        error.printStackTrace()
    }

    fun onComplete() {
        lastReceivedEvent.ifPresent { event ->
            eventPublisher.onNext(event)
        }
    }

}