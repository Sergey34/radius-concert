package seko0716.radius.concert.notification.services

import reactor.core.publisher.UnicastProcessor
import seko0716.radius.concert.notification.domains.Message
import java.util.*

class WebSocketMessageSubscriber constructor(
    private val eventPublisher: UnicastProcessor<Message>,
    private var lastReceivedEvent: Optional<Message> = Optional.empty()
) {
    fun onNext(event: Message) {
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