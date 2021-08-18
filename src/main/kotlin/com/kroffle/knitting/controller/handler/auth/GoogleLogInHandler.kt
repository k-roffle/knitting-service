package com.kroffle.knitting.controller.handler.auth

import com.kroffle.knitting.controller.handler.auth.dto.AuthorizedResponse
import com.kroffle.knitting.controller.handler.auth.dto.RefreshTokenResponse
import com.kroffle.knitting.controller.handler.exception.Unauthorized
import com.kroffle.knitting.controller.handler.helper.auth.AuthHelper
import com.kroffle.knitting.controller.handler.helper.response.ResponseHelper
import com.kroffle.knitting.usecase.auth.AuthService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.temporaryRedirect
import reactor.core.publisher.Mono

@Component
class GoogleLogInHandler(private val authService: AuthService) {
    fun requestCode(req: ServerRequest): Mono<ServerResponse> {
        return temporaryRedirect(authService.getAuthorizationUri()).build()
    }

    fun authorized(req: ServerRequest): Mono<ServerResponse> {
        val code = req.queryParam("code")
        if (code.isEmpty) {
            throw Unauthorized("code is required")
        }
        return authService
            .authorize(code.get())
            .map { AuthorizedResponse(it) }
            .flatMap {
                ResponseHelper.makeJsonResponse(it)
            }
    }

    fun refreshToken(req: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(req)
        return ResponseHelper
            .makeJsonResponse(
                RefreshTokenResponse(authService.refreshToken(knitterId))
            )
    }
}
