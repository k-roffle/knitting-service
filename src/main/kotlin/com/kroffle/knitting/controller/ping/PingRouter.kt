package com.kroffle.knitting.controller.ping

import com.kroffle.knitting.usecase.ping.PingHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.router

@Configuration
class PingRouter(private val handler: PingHandler) {
    @Bean
    fun pingRouterFunction() = nest(
        path("/ping"),
        router {
            listOf(GET("/", handler::get))
        }
    )
}
