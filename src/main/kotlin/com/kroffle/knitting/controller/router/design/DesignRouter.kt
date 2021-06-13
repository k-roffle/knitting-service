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
        path(ROOT_PATH),
        router {
            listOf(
                POST(CREATE_DESIGN_PATH, handler::createDesign),
            )
        }
    )

    companion object {
        private const val ROOT_PATH = "/design"
        private const val CREATE_DESIGN_PATH = "/"
        val PUBLIC_PATHS = listOf<String>()
    }
}
