package com.kroffle.knitting.controller.router.design

import com.kroffle.knitting.controller.handler.design.DesignHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.router

@Configuration
class DesignsRouter(private val handler: DesignHandler) {
    @Bean
    fun designsRouterFunction() = nest(
        path(ROOT_PATH),
        router {
            listOf(
                GET(GET_ALL_PATH, handler::getAll),
                GET(GET_MY_DESIGNS_PATH, handler::getMyDesigns),
            )
        }
    )

    companion object {
        private const val ROOT_PATH = "/designs"
        private const val GET_ALL_PATH = "/"
        private const val GET_MY_DESIGNS_PATH = "/my"
        val PUBLIC_PATHS = listOf(
            "${ROOT_PATH}$GET_ALL_PATH",
        )
    }
}
