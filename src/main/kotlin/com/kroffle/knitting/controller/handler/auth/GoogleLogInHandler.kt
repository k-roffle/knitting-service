package com.kroffle.knitting.controller.handler.auth

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.ServerResponse.temporaryRedirect
import reactor.core.publisher.Mono
import java.net.URI

@Component
class GoogleLogInHandler(private val oAuthHelper: GoogleOAuthHelper) {
    fun requestCode(req: ServerRequest): Mono<ServerResponse> {
        return temporaryRedirect(URI.create(oAuthHelper.getAuthorizationUri()))
            .build()
    }

    fun authorized(req: ServerRequest): Mono<ServerResponse> {
        // TODO implement
        return ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("todo")
    }

    interface GoogleOAuthHelper {
        fun getAuthorizationUri(): String
    }
}
