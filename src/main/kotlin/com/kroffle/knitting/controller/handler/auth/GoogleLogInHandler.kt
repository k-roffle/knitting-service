package com.kroffle.knitting.controller.handler.auth

import com.kroffle.knitting.usecase.auth.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.ServerResponse.temporaryRedirect
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import java.util.UUID

@Component
class GoogleLogInHandler(private val authService: AuthService) {
    fun requestCode(req: ServerRequest): Mono<ServerResponse> {
        return temporaryRedirect(authService.getAuthorizationUri()).build()
    }

    fun authorized(req: ServerRequest): Mono<ServerResponse> {
        return ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(authService.authorize())
    }

    fun refreshToken(req: ServerRequest): Mono<ServerResponse> {
        val userId = req.attribute("userId")
        if (userId.isEmpty) {
            return Mono.error(ResponseStatusException(HttpStatus.UNAUTHORIZED, "userId is required"))
        }
        return ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(authService.refreshToken(userId.get() as UUID))
    }
}
