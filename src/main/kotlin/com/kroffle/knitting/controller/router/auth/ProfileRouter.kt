package com.kroffle.knitting.controller.router.auth

import com.kroffle.knitting.controller.handler.auth.ProfileHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.router

@Configuration
class ProfileRouter(private val handler: ProfileHandler) {
    @Bean
    fun profileRouterFunction() = RouterFunctions.nest(
        RequestPredicates.path(ROOT_PATH),
        router {
            listOf(
                GET(handler::getMyProfile),
            )
        }
    )

    companion object {
        private const val ROOT_PATH = "/profile"
        val PUBLIC_PATHS: List<String> = listOf()
    }
}
