package com.kroffle.knitting.controller.router.design

import com.kroffle.knitting.controller.handler.design.DesignHandler
import com.kroffle.knitting.controller.handler.draftdesign.DraftDesignHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.router

@Configuration
class DesignsRouter(private val designHandler: DesignHandler, private val draftDesignHandler: DraftDesignHandler) {
    @Bean
    fun designsRouterFunction() = nest(
        path(ROOT_PATH),
        router {
            listOf(
                GET(GET_MY_DESIGNS_PATH, designHandler::getMyDesigns),
                GET(GET_MY_DRAFT_DESIGNS_PATH, draftDesignHandler::getMyDraftDesigns),
            )
        }
    )

    companion object {
        private const val ROOT_PATH = "/designs"
        private const val GET_MY_DESIGNS_PATH = "/my"
        private const val GET_MY_DRAFT_DESIGNS_PATH = "/draft/my"
        val PUBLIC_PATHS: List<String> = listOf()
    }
}
