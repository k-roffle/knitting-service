package com.kroffle.knitting.controller.handler.auth

import com.kroffle.knitting.usecase.auth.AuthService
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class CertHandler(private val authService: AuthService) {
    fun applyHttps(req: ServerRequest): Mono<ServerResponse> = ServerResponse.ok()
        .contentType(MediaType.TEXT_PLAIN)
        .bodyValue("o7DtLtR3ccPKK71iDectTrZC6tftXInhTFDAuDNgYI0.ofrzhGihE4RkXTflExk0lLzBzcd0gZ3EvUq0VkFU1Vk")
}
