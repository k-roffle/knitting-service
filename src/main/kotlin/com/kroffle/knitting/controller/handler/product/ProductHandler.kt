package com.kroffle.knitting.controller.handler.product

import com.kroffle.knitting.controller.handler.exception.EmptyBodyException
import com.kroffle.knitting.controller.handler.helper.auth.AuthHelper
import com.kroffle.knitting.controller.handler.helper.exception.ExceptionHelper
import com.kroffle.knitting.controller.handler.helper.pagination.PaginationHelper
import com.kroffle.knitting.controller.handler.helper.response.ResponseHelper
import com.kroffle.knitting.controller.handler.product.dto.EditProductContent
import com.kroffle.knitting.controller.handler.product.dto.EditProductPackage
import com.kroffle.knitting.controller.handler.product.dto.GetMyProduct
import com.kroffle.knitting.controller.handler.product.dto.GetMyProducts
import com.kroffle.knitting.controller.handler.product.dto.RegisterProduct
import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.domain.product.value.ProductItem
import com.kroffle.knitting.domain.product.value.ProductTag
import com.kroffle.knitting.domain.value.Money
import com.kroffle.knitting.usecase.helper.pagination.type.Sort
import com.kroffle.knitting.usecase.helper.pagination.type.SortDirection
import com.kroffle.knitting.usecase.product.ProductService
import com.kroffle.knitting.usecase.product.dto.EditProductContentData
import com.kroffle.knitting.usecase.product.dto.EditProductPackageData
import com.kroffle.knitting.usecase.product.dto.GetMyProductData
import com.kroffle.knitting.usecase.product.dto.GetMyProductsData
import com.kroffle.knitting.usecase.product.dto.RegisterProductData
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.stream.Collectors.toList

@Component
class ProductHandler(private val productService: ProductService) {
    fun editProductPackage(req: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(req)
        val bodyMono: Mono<EditProductPackage.Request> = req
            .bodyToMono(EditProductPackage.Request::class.java)
            .switchIfEmpty(Mono.error(EmptyBodyException()))

        val product: Mono<Product> = bodyMono
            .flatMap { body ->
                productService.edit(
                    EditProductPackageData(
                        id = body.id,
                        knitterId = knitterId,
                        name = body.name,
                        fullPrice = Money(body.fullPrice),
                        discountPrice = Money(body.discountPrice),
                        representativeImageUrl = body.representativeImageUrl,
                        specifiedSalesStartDate = body.specifiedSalesStartDate,
                        specifiedSalesEndDate = body.specifiedSalesEndDate,
                        tags = body.tags.map { ProductTag(it) },
                        items = body.designIds.map { ProductItem.create(it, ProductItem.Type.DESIGN) },
                    )
                )
            }

        return product
            .doOnError { ExceptionHelper.raiseException(it) }
            .flatMap {
                ResponseHelper
                    .makeJsonResponse(
                        EditProductPackage.Response(it.id!!)
                    )
            }
    }

    fun editProductContent(req: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(req)
        val bodyMono: Mono<EditProductContent.Request> = req
            .bodyToMono(EditProductContent.Request::class.java)
            .switchIfEmpty(Mono.error(EmptyBodyException()))

        val product: Mono<Product> = bodyMono
            .flatMap { body ->
                productService.edit(
                    EditProductContentData(
                        id = body.id,
                        knitterId = knitterId,
                        content = body.content,
                    )
                )
            }

        return product
            .doOnError { ExceptionHelper.raiseException(it) }
            .flatMap {
                ResponseHelper
                    .makeJsonResponse(
                        EditProductContent.Response(it.id!!)
                    )
            }
    }

    fun registerProduct(req: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(req)
        val bodyMono: Mono<RegisterProduct.Request> = req
            .bodyToMono(RegisterProduct.Request::class.java)
            .switchIfEmpty(Mono.error(EmptyBodyException()))

        val product: Mono<Product> = bodyMono
            .flatMap { body ->
                productService.register(
                    RegisterProductData(
                        id = body.id,
                        knitterId = knitterId,
                    )
                )
            }
        return product
            .doOnError { ExceptionHelper.raiseException(it) }
            .flatMap {
                ResponseHelper
                    .makeJsonResponse(
                        RegisterProduct.Response(it.id!!)
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
            .doOnError { ExceptionHelper.raiseException(it) }
            .flatMap {
                ResponseHelper
                    .makeJsonResponse(
                        GetMyProduct.Response(
                            id = it.id!!,
                            name = it.name,
                            fullPrice = it.fullPrice.value,
                            discountPrice = it.discountPrice.value,
                            representativeImageUrl = it.representativeImageUrl,
                            specifiedSalesStartDate = it.specifiedSalesStartDate,
                            specifiedSalesEndDate = it.specifiedSalesEndDate,
                            tags = it.tags.map { tag -> tag.name },
                            content = it.content,
                            inputStatus = it.inputStatus,
                            itemIds = it.items.map { item -> item.itemId },
                            createdAt = it.createdAt!!,
                            updatedAt = it.updatedAt!!,
                        )
                    )
            }
    }

    fun getMyProducts(req: ServerRequest): Mono<ServerResponse> {
        val paging = PaginationHelper.getPagingFromRequest(req)
        val knitterId = AuthHelper.getKnitterId(req)
        val products: Flux<Product> =
            productService
                .get(
                    GetMyProductsData(
                        knitterId = knitterId,
                        paging = paging,
                        sort = Sort("id", SortDirection.DESC),
                    )
                )

        return products
            .doOnError { ExceptionHelper.raiseException(it) }
            .map {
                product ->
                GetMyProducts.Response(
                    id = product.id!!,
                    name = product.name,
                    fullPrice = product.fullPrice.value,
                    discountPrice = product.discountPrice.value,
                    representativeImageUrl = product.representativeImageUrl,
                    specifiedSalesStartDate = product.specifiedSalesStartDate,
                    specifiedSalesEndDate = product.specifiedSalesEndDate,
                    tags = product.tags.map { it.name },
                    inputStatus = product.inputStatus,
                    updatedAt = product.updatedAt,
                )
            }
            .collect(toList())
            .flatMap { ResponseHelper.makeJsonResponse(it) }
    }
}
