package com.kroffle.knitting.controller.handler.product

import com.kroffle.knitting.controller.handler.exception.EmptyBodyException
import com.kroffle.knitting.controller.handler.helper.auth.AuthHelper
import com.kroffle.knitting.controller.handler.helper.exception.ExceptionHelper
import com.kroffle.knitting.controller.handler.helper.pagination.PaginationHelper
import com.kroffle.knitting.controller.handler.helper.response.ResponseHelper
import com.kroffle.knitting.controller.handler.product.dto.EditProductContent
import com.kroffle.knitting.controller.handler.product.dto.EditProductPackage
import com.kroffle.knitting.controller.handler.product.dto.RegisterProduct
import com.kroffle.knitting.controller.handler.product.mapper.ProductRequestMapper
import com.kroffle.knitting.controller.handler.product.mapper.ProductResponseMapper
import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.usecase.product.ProductService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
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
            .map { ProductRequestMapper.toEditProductPackageData(it, knitterId) }
            .flatMap(productService::edit)

        return product
            .doOnError(ExceptionHelper::raiseException)
            .map(ProductResponseMapper::toEditProductPackageResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun editProductContent(req: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(req)
        val bodyMono: Mono<EditProductContent.Request> = req
            .bodyToMono(EditProductContent.Request::class.java)
            .switchIfEmpty(Mono.error(EmptyBodyException()))

        val product: Mono<Product> = bodyMono
            .map { ProductRequestMapper.toEditProductContentData(it, knitterId) }
            .flatMap(productService::edit)

        return product
            .doOnError(ExceptionHelper::raiseException)
            .map(ProductResponseMapper::toEditProductContentResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun registerProduct(req: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(req)
        val bodyMono: Mono<RegisterProduct.Request> = req
            .bodyToMono(RegisterProduct.Request::class.java)
            .switchIfEmpty(Mono.error(EmptyBodyException()))

        val product: Mono<Product> = bodyMono
            .map { ProductRequestMapper.toRegisterProductData(it, knitterId) }
            .flatMap(productService::register)

        return product
            .doOnError(ExceptionHelper::raiseException)
            .map(ProductResponseMapper::toRegisterProductResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun getMyProduct(req: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(req)
        val productId = req.pathVariable("productId").toLong()

        return productService
            .get(ProductRequestMapper.toGetMyProductData(productId, knitterId))
            .doOnError(ExceptionHelper::raiseException)
            .map(ProductResponseMapper::toGetMyProductResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun getMyProducts(req: ServerRequest): Mono<ServerResponse> {
        val paging = PaginationHelper.getPagingFromRequest(req)
        val knitterId = AuthHelper.getKnitterId(req)

        return productService
            .get(ProductRequestMapper.toGetMyProductsData(paging, knitterId))
            .doOnError(ExceptionHelper::raiseException)
            .map(ProductResponseMapper::toGetMyProductsResponse)
            .collect(toList())
            .flatMap(ResponseHelper::makeJsonResponse)
    }
}
