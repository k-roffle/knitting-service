package com.kroffle.knitting.controller.handler.draftdesign

import com.kroffle.knitting.controller.handler.draftdesign.dto.DeleteDraftDesign
import com.kroffle.knitting.controller.handler.draftdesign.dto.GetMyDraftDesign
import com.kroffle.knitting.controller.handler.draftdesign.dto.GetMyDraftDesigns
import com.kroffle.knitting.controller.handler.draftdesign.dto.SaveDraftDesign
import com.kroffle.knitting.controller.handler.exception.EmptyBodyException
import com.kroffle.knitting.controller.handler.helper.auth.AuthHelper
import com.kroffle.knitting.controller.handler.helper.exception.ExceptionHelper
import com.kroffle.knitting.controller.handler.helper.response.ResponseHelper
import com.kroffle.knitting.usecase.draftdesign.DraftDesignService
import com.kroffle.knitting.usecase.draftdesign.dto.SaveDraftDesignData
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.stream.Collectors

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

    fun getMyDraftDesigns(req: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(req)
        return service
            .getMyDraftDesigns(knitterId)
            .doOnError { ExceptionHelper.raiseException(it) }
            .map { draftDesign ->
                GetMyDraftDesigns.Response(
                    id = draftDesign.id!!,
                    name = draftDesign.name,
                    updatedAt = draftDesign.updatedAt!!,
                )
            }
            .collect(Collectors.toList())
            .flatMap { ResponseHelper.makeJsonResponse(it) }
    }

    fun getMyDraftDesign(req: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(req)
        val draftDesignId = req.pathVariable("id").toLong()
        return service
            .getMyDraftDesign(draftDesignId, knitterId)
            .doOnError { ExceptionHelper.raiseException(it) }
            .map { draftDesign ->
                GetMyDraftDesign.Response(
                    id = draftDesign.id!!,
                    value = draftDesign.value,
                    updatedAt = draftDesign.updatedAt!!,
                )
            }
            .flatMap { ResponseHelper.makeJsonResponse(it) }
    }

    fun deleteMyDraftDesign(req: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(req)
        val draftDesignId = req.pathVariable("id").toLong()
        return service
            .deleteMyDraftDesign(draftDesignId, knitterId)
            .doOnError { ExceptionHelper.raiseException(it) }
            .map { deletedId -> DeleteDraftDesign.Response(id = deletedId) }
            .flatMap { ResponseHelper.makeJsonResponse(it) }
    }
}
