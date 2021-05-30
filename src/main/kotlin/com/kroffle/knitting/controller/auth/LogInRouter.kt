package com.kroffle.knitting.controller.auth

import com.kroffle.knitting.usecase.auth.GoogleLogInHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.router

@Configuration
class LogInRouter(private val handler: GoogleLogInHandler) {
    @Bean
    fun logInRouterFunction() = RouterFunctions.nest(
        RequestPredicates.path("/auth"),
        router {
            listOf(
                POST("/google/code", handler::requestCode),
                GET("/google/authorized", handler::authorized),
            )
        }
    )
}
