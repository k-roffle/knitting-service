package com.kroffle.knitting.controller.handler.auth

import com.kroffle.knitting.controller.handler.auth.exception.NotFoundCode
import com.kroffle.knitting.controller.handler.auth.mapper.GoogleLoginResponseMapper
import com.kroffle.knitting.controller.handler.helper.auth.AuthHelper
import com.kroffle.knitting.controller.handler.helper.exception.ExceptionHelper
import com.kroffle.knitting.controller.handler.helper.response.ResponseHelper
import com.kroffle.knitting.usecase.auth.AuthService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.temporaryRedirect
import reactor.core.publisher.Mono

@Component
class GoogleLogInHandler(private val authService: AuthService) {
    fun requestCode(request: ServerRequest): Mono<ServerResponse> {
        return temporaryRedirect(authService.getAuthorizationUri()).build()
    }

    fun authorized(request: ServerRequest): Mono<ServerResponse> {
        val code = request.queryParam("code")
        if (code.isEmpty || code.get().isBlank()) {
            ExceptionHelper.raiseException(NotFoundCode())
        }
        return authService
            .authorize(code.get())
            .doOnError(ExceptionHelper::raiseException)
            .map(GoogleLoginResponseMapper::toAuthorizedResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun refreshToken(request: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(request)
        val refreshToken = authService.refreshToken(knitterId)
        val response = GoogleLoginResponseMapper.toRefreshTokenResponse(refreshToken)
        return ResponseHelper.makeJsonResponse(response)
    }
}
