package com.kroffle.knitting.controller.handler.knitter

import com.kroffle.knitting.controller.handler.helper.auth.AuthHelper
import com.kroffle.knitting.controller.handler.helper.exception.ExceptionHelper
import com.kroffle.knitting.controller.handler.helper.response.ResponseHelper
import com.kroffle.knitting.controller.handler.knitter.dto.MyProfile
import com.kroffle.knitting.controller.handler.knitter.dto.SalesSummary
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
    fun getMyProfile(req: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(req)
        return knitterService
            .getKnitter(knitterId)
            .doOnError { ExceptionHelper.raiseException(it) }
            .map { knitter ->
                MyProfile.Response(
                    email = knitter.email,
                    name = knitter.name,
                    profileImageUrl = knitter.profileImageUrl,
                )
            }
            .flatMap {
                ResponseHelper.makeJsonResponse(it)
            }
    }

    fun getMySalesSummary(req: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(req)
        return productSummaryService
            .countProductOnList(knitterId)
            .doOnError { ExceptionHelper.raiseException(it) }
            .flatMap {
                ResponseHelper
                    .makeJsonResponse(
                        SalesSummary.Response(
                            numberOfProductsOnSales = it,
                            numberOfProductsSold = 0,
                        )
                    )
            }
    }
}
