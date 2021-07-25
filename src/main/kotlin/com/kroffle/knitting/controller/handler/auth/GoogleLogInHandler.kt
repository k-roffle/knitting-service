package com.kroffle.knitting.controller.handler.auth

import com.kroffle.knitting.controller.handler.auth.model.AuthorizedResponse
import com.kroffle.knitting.controller.handler.auth.model.RefreshTokenResponse
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

@Component
class GoogleLogInHandler(private val authService: AuthService) {
    fun requestCode(req: ServerRequest): Mono<ServerResponse> {
        return temporaryRedirect(authService.getAuthorizationUri()).build()
    }

    fun authorized(req: ServerRequest): Mono<ServerResponse> {
        val code = req.queryParam("code")
        if (code.isEmpty) {
            return Mono.error(ResponseStatusException(HttpStatus.UNAUTHORIZED, "code is required"))
        }
        return authService.authorize(code.get()).flatMap {
            ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(AuthorizedResponse(it))
        }
    }

    fun refreshToken(req: ServerRequest): Mono<ServerResponse> {
        val userId = req.attribute("userId")
        if (userId.isEmpty) {
            return Mono.error(ResponseStatusException(HttpStatus.UNAUTHORIZED, "userId is required"))
        }
        return ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(RefreshTokenResponse(authService.refreshToken(userId.get() as Long)))
    }
}
