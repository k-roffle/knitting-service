package com.kroffle.knitting.usecase.auth

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.ServerResponse.temporaryRedirect
import org.springframework.web.server.ServerWebInputException
import reactor.core.publisher.Mono
import java.net.URI
import org.springframework.web.reactive.function.client.WebClient



@Component
class GoogleLogInHandler(private val oAuthHelper: GoogleOAuthHelper) {
    fun requestCode(req: ServerRequest): Mono<ServerResponse> {
        return temporaryRedirect(URI.create(oAuthHelper.getAuthorizationUri()))
            .build()
    }

    fun authorized(req: ServerRequest): Mono<ServerResponse> {
        val googleToken = req.queryParam("code").map {
            oAuthHelper.getGoogleAccessToken(it)
        }.orElse(
            Mono.just("failed~")
        )
        return googleToken.flatMap {
            ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(it)
        }
    }

    interface GoogleOAuthHelper {
        fun getAuthorizationUri(): String
        fun getGoogleAccessToken(code: String): Mono<String>
    }
}
