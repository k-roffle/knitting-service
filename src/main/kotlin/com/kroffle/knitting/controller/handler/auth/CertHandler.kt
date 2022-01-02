package com.kroffle.knitting.controller.handler.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class CertHandler(
    @Value("\${cert.secret}")
    private val certSecret: String,
) {
    fun applyHttps(request: ServerRequest): Mono<ServerResponse> = ServerResponse.ok()
        .contentType(MediaType.TEXT_PLAIN)
        .bodyValue(certSecret)
}
