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
                GET(GET_MY_DESIGNS_PATH, handler::getMyDesigns),
                GET(GET_MY_SALES_SUMMARY_PATH, handler::getMySalesSummary),
            )
        }
    )

    companion object {
        private const val ROOT_PATH = "/designs"
        private const val GET_MY_DESIGNS_PATH = "/my"
        private const val GET_MY_SALES_SUMMARY_PATH = "/sales-summary/my"
        val PUBLIC_PATHS: List<String> = listOf()
    }
}
