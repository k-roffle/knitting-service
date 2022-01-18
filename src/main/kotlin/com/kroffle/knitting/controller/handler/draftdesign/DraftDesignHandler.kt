package com.kroffle.knitting.controller.handler.draftdesign

import com.kroffle.knitting.controller.handler.draftdesign.dto.SaveDraftDesign
import com.kroffle.knitting.controller.handler.draftdesign.mapper.DraftDesignRequestMapper
import com.kroffle.knitting.controller.handler.draftdesign.mapper.DraftDesignResponseMapper
import com.kroffle.knitting.controller.handler.helper.auth.AuthHelper
import com.kroffle.knitting.controller.handler.helper.exception.ExceptionHelper
import com.kroffle.knitting.controller.handler.helper.extension.ServerRequestExtension.getLongPathVariable
import com.kroffle.knitting.controller.handler.helper.extension.ServerRequestExtension.safetyBodyToMono
import com.kroffle.knitting.controller.handler.helper.response.ResponseHelper
import com.kroffle.knitting.usecase.draftdesign.DraftDesignService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.stream.Collectors

@Component
class DraftDesignHandler(private val service: DraftDesignService) {
    fun saveDraft(request: ServerRequest): Mono<ServerResponse> {
        val body = request.safetyBodyToMono(SaveDraftDesign.Request::class.java)
        val knitterId = AuthHelper.getKnitterId(request)
        return body
            .map { DraftDesignRequestMapper.toSaveDraftDesignData(it, knitterId) }
            .flatMap(service::saveDraft)
            .doOnError(ExceptionHelper::raiseException)
            .map(DraftDesignResponseMapper::toSaveDraftDesignResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun getMyDraftDesigns(request: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(request)
        return service
            .getMyDraftDesigns(knitterId)
            .doOnError(ExceptionHelper::raiseException)
            .map(DraftDesignResponseMapper::toGetMyDraftDesignsResponse)
            .collect(Collectors.toList())
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun getMyDraftDesign(request: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(request)
        val draftDesignId = request.getLongPathVariable("draftDesignId")
        return service
            .getMyDraftDesign(draftDesignId, knitterId)
            .doOnError(ExceptionHelper::raiseException)
            .map(DraftDesignResponseMapper::toGetMyDraftDesignResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun getMyDraftDesignToUpdate(request: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(request)
        val designId = request.getLongPathVariable("designId")
        return service
            .getMyDraftDesignToUpdate(designId = designId, knitterId = knitterId)
            .doOnError(ExceptionHelper::raiseException)
            .map(DraftDesignResponseMapper::toGetMyDraftDesignToUpdateResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun deleteMyDraftDesign(request: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(request)
        val draftDesignId = request.getLongPathVariable("draftDesignId")
        return service
            .deleteMyDraftDesign(draftDesignId, knitterId)
            .doOnError(ExceptionHelper::raiseException)
            .map(DraftDesignResponseMapper::toDeleteDraftDesignResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }
}
