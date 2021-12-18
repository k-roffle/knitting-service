package com.kroffle.knitting.controller.router.design

import com.kroffle.knitting.controller.handler.design.DesignHandler
import com.kroffle.knitting.controller.handler.draftdesign.DraftDesignHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.router

@Configuration
class DesignRouter(
    private val designHandler: DesignHandler,
    private val draftDesignHandler: DraftDesignHandler,
) {
    @Bean
    fun designRouterFunction() = nest(
        path(ROOT_PATH),
        router {
            listOf(
                POST(CREATE_DESIGN_PATH, designHandler::createDesign),
                POST(SAVE_DRAFT_PATH, draftDesignHandler::saveDraft),
                DELETE(DELETE_MY_DRAFT_DESIGN_PATH, draftDesignHandler::deleteMyDraftDesign),
            )
        }
    )

    companion object {
        private const val ROOT_PATH = "/design"
        private const val CREATE_DESIGN_PATH = ""
        private const val SAVE_DRAFT_PATH = "/draft"
        private const val DELETE_MY_DRAFT_DESIGN_PATH = "/draft/mine/{draftDesignId}"
        val PUBLIC_PATHS = listOf<String>()
    }
}
