package com.kroffle.knitting.domain.handler.ping

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono

@Component
class PingHandler {
    fun get(req: ServerRequest): Mono<ServerResponse> = ok()
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("pong")
}
