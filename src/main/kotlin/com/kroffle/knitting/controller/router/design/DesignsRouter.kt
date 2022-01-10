package com.kroffle.knitting.controller.router.design

import com.kroffle.knitting.controller.handler.design.DesignHandler
import com.kroffle.knitting.controller.handler.draftdesign.DraftDesignHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.router

@Configuration
class DesignsRouter(
    private val designHandler: DesignHandler,
    private val draftDesignHandler: DraftDesignHandler,
) {
    @Bean
    fun designsRouterFunction() = nest(
        path(ROOT_PATH),
        router {
            listOf(
                GET(GET_MY_DESIGNS_PATH, designHandler::getMyDesigns),
                POST(CREATE_DESIGN_PATH, designHandler::createDesign),
                PUT(UPDATE_DESIGN_PATH, designHandler::updateDesign),
            )
        }
    )

    @Bean
    fun draftDesignsRouterFunction() = nest(
        path(ROOT_PATH),
        router {
            listOf(
                GET(GET_MY_DRAFT_DESIGNS_PATH, draftDesignHandler::getMyDraftDesigns),
                GET(GET_MY_DRAFT_DESIGN_PATH, draftDesignHandler::getMyDraftDesign),
                GET(GET_MY_DRAFT_DESIGN_TO_UPDATE_PATH, draftDesignHandler::getMyDraftDesignToUpdate),
                POST(SAVE_DRAFT_PATH, draftDesignHandler::saveDraft),
                DELETE(DELETE_MY_DRAFT_DESIGN_PATH, draftDesignHandler::deleteMyDraftDesign),
            )
        }
    )

    companion object {
        private const val ROOT_PATH = "/designs"
        // path of design router
        private const val GET_MY_DESIGNS_PATH = "/mine"
        private const val CREATE_DESIGN_PATH = ""
        private const val UPDATE_DESIGN_PATH = "/{designId}"
        // path of draft design router
        private const val GET_MY_DRAFT_DESIGNS_PATH = "/draft/mine"
        private const val GET_MY_DRAFT_DESIGN_PATH = "/draft/mine/{draftDesignId}"
        private const val GET_MY_DRAFT_DESIGN_TO_UPDATE_PATH = "/{designId}/draft/mine"
        private const val SAVE_DRAFT_PATH = "/draft"
        private const val DELETE_MY_DRAFT_DESIGN_PATH = "/draft/mine/{draftDesignId}"

        val PUBLIC_PATHS: List<String> = listOf()
    }
}
