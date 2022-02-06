package com.kroffle.knitting.controller.handler.draftproduct

import com.kroffle.knitting.controller.handler.draftproduct.dto.SaveDraftProduct
import com.kroffle.knitting.controller.handler.draftproduct.mapper.DraftProductRequestMapper
import com.kroffle.knitting.controller.handler.draftproduct.mapper.DraftProductResponseMapper
import com.kroffle.knitting.controller.handler.helper.auth.AuthHelper
import com.kroffle.knitting.controller.handler.helper.exception.ExceptionHelper
import com.kroffle.knitting.controller.handler.helper.extension.ServerRequestExtension.getLongPathVariable
import com.kroffle.knitting.controller.handler.helper.extension.ServerRequestExtension.safetyBodyToMono
import com.kroffle.knitting.controller.handler.helper.response.ResponseHelper
import com.kroffle.knitting.usecase.draftproduct.DraftProductService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.stream.Collectors

@Component
class DraftProductHandler(private val service: DraftProductService) {
    fun saveDraft(request: ServerRequest): Mono<ServerResponse> {
        val body = request.safetyBodyToMono(SaveDraftProduct.Request::class.java)
        val knitterId = AuthHelper.getKnitterId(request)
        return body
            .map { DraftProductRequestMapper.toSaveDraftProductData(it, knitterId) }
            .flatMap(service::saveDraft)
            .doOnError(ExceptionHelper::raiseException)
            .map(DraftProductResponseMapper::toSaveDraftProductResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun getMyDraftProducts(request: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(request)
        return service
            .getMyDraftProducts(knitterId)
            .doOnError(ExceptionHelper::raiseException)
            .map(DraftProductResponseMapper::toGetMyDraftProductsResponse)
            .collect(Collectors.toList())
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun getMyDraftProduct(request: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(request)
        val draftProductId = request.getLongPathVariable("draftProductId")
        return service
            .getMyDraftProduct(draftProductId, knitterId)
            .doOnError(ExceptionHelper::raiseException)
            .map(DraftProductResponseMapper::toGetMyDraftProductResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun getMyDraftProductToUpdate(request: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(request)
        val productId = request.getLongPathVariable("productId")
        return service
            .getMyDraftProductToUpdate(productId = productId, knitterId = knitterId)
            .doOnError(ExceptionHelper::raiseException)
            .map(DraftProductResponseMapper::toGetMyDraftProductToUpdateResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun deleteMyDraftProduct(request: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(request)
        val draftProductId = request.getLongPathVariable("draftProductId")
        return service
            .deleteMyDraftProduct(draftProductId, knitterId)
            .doOnError(ExceptionHelper::raiseException)
            .map(DraftProductResponseMapper::toDeleteDraftProductResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }
}
