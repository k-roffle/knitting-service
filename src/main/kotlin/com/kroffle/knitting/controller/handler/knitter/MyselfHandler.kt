package com.kroffle.knitting.controller.handler.knitter

import com.kroffle.knitting.controller.handler.helper.auth.AuthHelper
import com.kroffle.knitting.controller.handler.helper.exception.ExceptionHelper
import com.kroffle.knitting.controller.handler.helper.response.ResponseHelper
import com.kroffle.knitting.controller.handler.knitter.mapper.MyselfResponseMapper
import com.kroffle.knitting.usecase.knitter.KnitterService
import com.kroffle.knitting.usecase.summary.ProductSummaryService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class MyselfHandler(
    private val knitterService: KnitterService,
    private val productSummaryService: ProductSummaryService,
) {
    fun getMyProfile(request: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(request)
        return knitterService
            .getKnitter(knitterId)
            .doOnError(ExceptionHelper::raiseException)
            .map(MyselfResponseMapper::toMyProfileResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun getMySalesSummary(request: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(request)
        return productSummaryService
            .countProductOnList(knitterId)
            .doOnError(ExceptionHelper::raiseException)
            .map(MyselfResponseMapper::toSalesSummaryResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }
}
