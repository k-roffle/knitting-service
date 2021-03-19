package com.kroffle.knitting.router

import com.kroffle.knitting.handler.DesignHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.router

@Configuration
class DesignRouter(private val handler: DesignHandler) {
    @Bean
    fun designRouterFunction() = nest(
        path("/designs"),
        router {
            listOf(GET("/", handler::getAll))
        }
    )
}
