package com.kroffle.knitting.controller.router.design

import com.kroffle.knitting.controller.handler.design.DesignHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.router

@Configuration
class DesignRouter(private val handler: DesignHandler) {
    @Bean
    fun designRouterFunction() = nest(
        path("/design"),
        router {
            listOf(
                POST("/", handler::createDesign),
            )
        }
    )
}
