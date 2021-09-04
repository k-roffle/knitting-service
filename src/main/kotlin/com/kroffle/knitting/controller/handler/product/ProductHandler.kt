package com.kroffle.knitting.controller.handler.product

import com.kroffle.knitting.controller.handler.exception.BadRequest
import com.kroffle.knitting.controller.handler.exception.EmptyBodyException
import com.kroffle.knitting.controller.handler.helper.auth.AuthHelper
import com.kroffle.knitting.controller.handler.helper.response.ResponseHelper
import com.kroffle.knitting.controller.handler.product.dto.DraftProductContentRequest
import com.kroffle.knitting.controller.handler.product.dto.DraftProductContentResponse
import com.kroffle.knitting.controller.handler.product.dto.DraftProductPackageRequest
import com.kroffle.knitting.controller.handler.product.dto.DraftProductPackageResponse
import com.kroffle.knitting.controller.handler.product.dto.GetMyProductResponse
import com.kroffle.knitting.controller.handler.product.dto.RegisterProductRequest
import com.kroffle.knitting.controller.handler.product.dto.RegisterProductResponse
import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.domain.product.enum.ProductItemType
import com.kroffle.knitting.domain.product.value.ProductItem
import com.kroffle.knitting.domain.product.value.ProductTag
import com.kroffle.knitting.domain.value.Money
import com.kroffle.knitting.usecase.product.ProductService
import com.kroffle.knitting.usecase.product.dto.DraftProductContentData
import com.kroffle.knitting.usecase.product.dto.DraftProductPackageData
import com.kroffle.knitting.usecase.product.dto.GetMyProductData
import com.kroffle.knitting.usecase.product.dto.RegisterProductData
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
                DraftProductPackageData(
                    id = body.id,
                    knitterId = knitterId,
                    name = body.name,
                    fullPrice = Money(body.fullPrice),
                    discountPrice = Money(body.discountPrice),
                    representativeImageUrl = body.representativeImageUrl,
                    specifiedSalesStartDate = body.specifiedSalesStartDate,
                    specifiedSalesEndDate = body.specifiedSalesEndDate,
                    tags = body.tags.map { ProductTag(null, it, null) },
                    items = body.designIds.map { ProductItem.create(null, it, null, ProductItemType.DESIGN) },
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

    fun draftProductContent(req: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(req)
        val bodyMono: Mono<DraftProductContentRequest> = req
            .bodyToMono(DraftProductContentRequest::class.java)
            .switchIfEmpty(Mono.error(EmptyBodyException()))

        val product: Mono<Product> = bodyMono.flatMap {
            body ->
            productService.draft(
                DraftProductContentData(
                    id = body.id,
                    knitterId = knitterId,
                    content = body.content,
                )
            )
        }

        return product
            .onErrorResume {
                error ->
                Mono.error(BadRequest(error.message))
            }
            .flatMap {
                ResponseHelper
                    .makeJsonResponse(
                        DraftProductContentResponse(it.id!!)
                    )
            }
    }

    fun registerProduct(req: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(req)
        val bodyMono: Mono<RegisterProductRequest> = req
            .bodyToMono(RegisterProductRequest::class.java)
            .switchIfEmpty(Mono.error(EmptyBodyException()))

        val product: Mono<Product> = bodyMono.flatMap {
            body ->
            productService.register(
                RegisterProductData(
                    id = body.id,
                    knitterId = knitterId,
                )
            )
        }
        return product
            .onErrorResume {
                Mono.error(BadRequest(it.message))
            }
            .flatMap {
                ResponseHelper
                    .makeJsonResponse(
                        RegisterProductResponse(it.id!!)
                    )
            }
    }

    fun getMyProduct(req: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(req)
        val productId = req.pathVariable("id").toLong()
        val product: Mono<Product> =
            productService.get(
                GetMyProductData(
                    id = productId,
                    knitterId = knitterId,
                )
            )
        return product
            .onErrorResume {
                Mono.error(BadRequest(it.message))
            }
            .flatMap {
                ResponseHelper
                    .makeJsonResponse(
                        GetMyProductResponse(
                            id = it.id!!,
                            name = it.name,
                            fullPrice = it.fullPrice.value,
                            discountPrice = it.discountPrice.value,
                            representativeImageUrl = it.representativeImageUrl,
                            specifiedSalesStartDate = it.specifiedSalesStartDate,
                            specifiedSalesEndDate = it.specifiedSalesEndDate,
                            tags = it.tags,
                            content = it.content,
                            inputStatus = it.inputStatus,
                            items = it.items,
                            createdAt = it.createdAt!!,
                            updatedAt = it.updatedAt!!,
                        )
                    )
            }
    }
}
