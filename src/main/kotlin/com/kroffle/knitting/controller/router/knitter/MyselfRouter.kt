package com.kroffle.knitting.controller.router.knitter

import com.kroffle.knitting.controller.handler.knitter.MyselfHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.router

@Configuration
class MyselfRouter(private val handler: MyselfHandler) {
    @Bean
    fun profileRouterFunction() = RouterFunctions.nest(
        RequestPredicates.path(ROOT_PATH),
        router {
            listOf(
                GET(GET_MY_PROFILE_PATH, handler::getMyProfile),
                GET(GET_MY_SALES_SUMMARY_PATH, handler::getMySalesSummary),
            )
        }
    )

    companion object {
        private const val ROOT_PATH = "/me"
        private const val GET_MY_PROFILE_PATH = "/profile"
        private const val GET_MY_SALES_SUMMARY_PATH = "/sales-summary"
        val PUBLIC_PATHS: List<String> = listOf()
    }
}
