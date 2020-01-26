package seko0716.radius.concert.notification.configuration

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter
import reactor.core.publisher.Flux
import reactor.core.publisher.UnicastProcessor


@Configuration
class ReactiveWebSocketConfiguration {
    @Bean
    fun webSocketHandlerMapping(
        @Qualifier("reactiveWebSocketHandler") webSocketHandler: WebSocketHandler
    ): HandlerMapping {
        val map: MutableMap<String, WebSocketHandler> = mutableMapOf("/event-emitter" to webSocketHandler)
        return SimpleUrlHandlerMapping()
            .apply {
                order = 1
                urlMap = map
            }
    }

    @Bean
    fun handlerAdapter(): WebSocketHandlerAdapter {
        return WebSocketHandlerAdapter()
    }

    @Bean
    fun eventPublisher(): UnicastProcessor<Map<String, String>> {
        return UnicastProcessor.create<Map<String, String>>()
    }

    @Bean
    fun events(eventPublisher: UnicastProcessor<Map<String, String>>): Flux<Map<String, String>> {
        return eventPublisher
            .replay(25)
            .autoConnect()
    }

}