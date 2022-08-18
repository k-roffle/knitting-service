package com.kroffle.knitting.controller.router.auth

import com.kroffle.knitting.controller.handler.auth.CertHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.router

@Configuration
class CertRouter(private val handler: CertHandler) {
    @Bean
    fun certRouterFunction() = RouterFunctions.nest(
        RequestPredicates.path(ROOT_PATH),
        router {
            listOf(
                GET(APPLY_HTTPS_PATH, handler::applyHttps),
            )
        }
    )

    companion object {
        private const val ROOT_PATH = "/"
        private const val APPLY_HTTPS_PATH = ".well-known/acme-challenge/aHbmbOpY1OKIgLcx0eNOsSM6KA_yaeIkaEM0ZYaTJE8"
        val PUBLIC_PATHS = listOf(
            "$ROOT_PATH$APPLY_HTTPS_PATH",
        )
    }
}
