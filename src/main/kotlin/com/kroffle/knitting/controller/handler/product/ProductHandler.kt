package com.kroffle.knitting.controller.handler.product

import com.kroffle.knitting.controller.handler.exception.EmptyBodyException
import com.kroffle.knitting.controller.handler.helper.auth.AuthHelper
import com.kroffle.knitting.controller.handler.helper.response.ResponseHelper
import com.kroffle.knitting.controller.handler.product.dto.DraftProductPackageRequest
import com.kroffle.knitting.controller.handler.product.dto.DraftProductPackageResponse
import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.domain.value.Money
import com.kroffle.knitting.usecase.product.ProductService
import com.kroffle.knitting.usecase.product.dto.DraftProductPackage
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class ProductHandler(private val productService: ProductService) {
    fun draftProductPackage(req: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(req)
        val bodyMono: Mono<DraftProductPackageRequest> = req
            .bodyToMono(DraftProductPackageRequest::class.java)
            .switchIfEmpty(Mono.error(EmptyBodyException()))

        val product: Mono<Product> = bodyMono.flatMap {
            body ->
            productService.draft(
                DraftProductPackage(
                    knitterId = knitterId,
                    name = body.name,
                    fullPrice = Money(body.fullPrice),
                    discountPrice = Money(body.discountPrice),
                    representativeImageUrl = body.representativeImageUrl,
                    specifiedSalesStartDate = body.specifiedSalesStartDate,
                    specifiedSalesEndDate = body.specifiedSalesEndDate,
                    tags = body.tags,
                    designIds = body.designIds,
                    goodsIds = listOf(),
                )
            )
        }

        return product
            .flatMap {
                ResponseHelper
                    .makeJsonResponse(
                        DraftProductPackageResponse(it.id!!)
                    )
            }
    }
}
