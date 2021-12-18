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
                GET(GET_MY_DRAFT_DESIGN_PATH, draftDesignHandler::getMyDraftDesign),
            )
        }
    )

    companion object {
        private const val ROOT_PATH = "/designs"
        private const val GET_MY_DESIGNS_PATH = "/mine"
        private const val GET_MY_DRAFT_DESIGNS_PATH = "/draft/mine"
        private const val GET_MY_DRAFT_DESIGN_PATH = "/draft/mine/{id}"
        val PUBLIC_PATHS: List<String> = listOf()
    }
}
