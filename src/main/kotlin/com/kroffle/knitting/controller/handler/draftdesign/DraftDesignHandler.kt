package com.kroffle.knitting.controller.handler.draftdesign

import com.kroffle.knitting.controller.handler.draftdesign.dto.SaveDraftDesign
import com.kroffle.knitting.controller.handler.exception.EmptyBodyException
import com.kroffle.knitting.controller.handler.helper.auth.AuthHelper
import com.kroffle.knitting.controller.handler.helper.response.ResponseHelper
import com.kroffle.knitting.usecase.draftdesign.DraftDesignService
import com.kroffle.knitting.usecase.draftdesign.dto.SaveDraftDesignData
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class DraftDesignHandler(private val service: DraftDesignService) {
    fun saveDraft(req: ServerRequest): Mono<ServerResponse> {
        // TODO: Add unit test
        val body: Mono<SaveDraftDesign.Request> = req
            .bodyToMono(SaveDraftDesign.Request::class.java)
            .switchIfEmpty(Mono.error(EmptyBodyException()))
        val knitterId = AuthHelper.getKnitterId(req)
        return body
            .flatMap {
                service
                    .saveDraft(
                        SaveDraftDesignData(
                            id = it.id,
                            knitterId = knitterId,
                            designId = it.designId,
                            value = it.value,
                        )
                    )
            }
            .map { SaveDraftDesign.Response(it.id!!) }
            .flatMap { ResponseHelper.makeJsonResponse(it) }
    }
}
