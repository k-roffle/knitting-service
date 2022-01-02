package com.kroffle.knitting.controller.handler.draftdesign

import com.kroffle.knitting.controller.handler.draftdesign.dto.SaveDraftDesign
import com.kroffle.knitting.controller.handler.draftdesign.mapper.DraftDesignRequestMapper
import com.kroffle.knitting.controller.handler.draftdesign.mapper.DraftDesignResponseMapper
import com.kroffle.knitting.controller.handler.exception.EmptyBodyException
import com.kroffle.knitting.controller.handler.exception.InvalidBodyException
import com.kroffle.knitting.controller.handler.helper.auth.AuthHelper
import com.kroffle.knitting.controller.handler.helper.exception.ExceptionHelper
import com.kroffle.knitting.controller.handler.helper.response.ResponseHelper
import com.kroffle.knitting.usecase.draftdesign.DraftDesignService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.stream.Collectors

@Component
class DraftDesignHandler(private val service: DraftDesignService) {
    fun saveDraft(req: ServerRequest): Mono<ServerResponse> {
        val body: Mono<SaveDraftDesign.Request> = req
            .bodyToMono(SaveDraftDesign.Request::class.java)
            .onErrorResume { Mono.error(InvalidBodyException()) }
            .switchIfEmpty(Mono.error(EmptyBodyException()))
        val knitterId = AuthHelper.getKnitterId(req)
        return body
            .map { DraftDesignRequestMapper.toSaveDraftDesignData(it, knitterId) }
            .flatMap(service::saveDraft)
            .doOnError(ExceptionHelper::raiseException)
            .map(DraftDesignResponseMapper::toSaveDraftDesignResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun getMyDraftDesigns(req: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(req)
        return service
            .getMyDraftDesigns(knitterId)
            .doOnError(ExceptionHelper::raiseException)
            .map(DraftDesignResponseMapper::toGetMyDraftDesignsResponse)
            .collect(Collectors.toList())
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun getMyDraftDesign(req: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(req)
        val draftDesignId = req.pathVariable("draftDesignId").toLong()
        return service
            .getMyDraftDesign(draftDesignId, knitterId)
            .doOnError(ExceptionHelper::raiseException)
            .map(DraftDesignResponseMapper::toGetMyDraftDesignResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun getMyDraftDesignToUpdate(req: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(req)
        val designId = req.pathVariable("designId").toLong()
        return service
            .getMyDraftDesignToUpdate(designId = designId, knitterId = knitterId)
            .doOnError(ExceptionHelper::raiseException)
            .map(DraftDesignResponseMapper::toGetMyDraftDesignToUpdateResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun deleteMyDraftDesign(req: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(req)
        val draftDesignId = req.pathVariable("draftDesignId").toLong()
        return service
            .deleteMyDraftDesign(draftDesignId, knitterId)
            .doOnError(ExceptionHelper::raiseException)
            .map(DraftDesignResponseMapper::toDeleteDraftDesignResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }
}
