package com.kroffle.knitting.helper

import com.kroffle.knitting.controller.filter.auth.AuthorizationFilter
import com.kroffle.knitting.infra.jwt.TokenDecoder
import com.kroffle.knitting.infra.jwt.TokenPublisher
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse

class WebTestClientHelper {
    companion object {
        const val AUTHORIZED_KNITTER_ID: Long = 1
        private const val JWT_SECRET_KEY = "I'M SECRET KEY!"
        private val tokenDecoder = TokenDecoder(JWT_SECRET_KEY)
        private val tokenPublisher = TokenPublisher(JWT_SECRET_KEY)
        private val token = tokenPublisher.publish(AUTHORIZED_KNITTER_ID)

        fun createWebTestClient(routerFunction: RouterFunction<ServerResponse>): WebTestClient {
            return WebTestClient
                .bindToRouterFunction(routerFunction)
                .webFilter<WebTestClient.RouterFunctionSpec>(AuthorizationFilter(tokenDecoder))
                .build()
        }

        fun addDefaultRequestHeader(
            request: WebTestClient.RequestBodySpec,
            authorized: Boolean = true,
            mediaType: MediaType = MediaType.APPLICATION_JSON,
        ): WebTestClient.RequestBodySpec {
            val requestWithHeader =
                if (authorized) {
                    request
                        .header("Authorization", "Bearer $token")
                } else {
                    request
                }

            return requestWithHeader
                .accept(mediaType)
                .contentType(mediaType)
        }

        fun addDefaultRequestHeader(
            request: WebTestClient.RequestHeadersSpec<*>,
            authorized: Boolean = true,
            mediaType: MediaType = MediaType.APPLICATION_JSON,
        ): WebTestClient.RequestHeadersSpec<*> {
            val requestWithHeader =
                if (authorized) {
                    request
                        .header("Authorization", "Bearer $token")
                } else {
                    request
                }

            return requestWithHeader
                .accept(mediaType)
        }
    }
}
