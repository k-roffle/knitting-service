package com.kroffle.knitting.controller.router.auth

import com.kroffle.knitting.controller.handler.auth.GoogleLogInHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.router

@Configuration
class LogInRouter(private val handler: GoogleLogInHandler) {
    @Bean
    fun logInRouterFunction() = RouterFunctions.nest(
        RequestPredicates.path(ROOT_PATH),
        router {
            listOf(
                GET(REQUEST_CODE_PATH, handler::requestCode),
                GET(AUTHORIZED_PATH, handler::authorized),
                POST(REFRESH_TOKEN_PATH, handler::refreshToken),
            )
        }
    )

    companion object {
        private const val ROOT_PATH = "/auth"
        private const val REQUEST_CODE_PATH = "/google/code"
        private const val AUTHORIZED_PATH = "/google/authorized"
        private const val REFRESH_TOKEN_PATH = "/refresh"
        val PUBLIC_PATHS = listOf(
            "$ROOT_PATH$REQUEST_CODE_PATH",
            "$ROOT_PATH$AUTHORIZED_PATH",
        )
    }
}
