package com.kroffle.knitting.controller.handler.product

import com.kroffle.knitting.controller.handler.helper.auth.AuthHelper
import com.kroffle.knitting.controller.handler.helper.exception.ExceptionHelper
import com.kroffle.knitting.controller.handler.helper.extension.ServerRequestExtension.getLongPathVariable
import com.kroffle.knitting.controller.handler.helper.extension.ServerRequestExtension.safetyBodyToMono
import com.kroffle.knitting.controller.handler.helper.pagination.PaginationHelper
import com.kroffle.knitting.controller.handler.helper.response.ResponseHelper
import com.kroffle.knitting.controller.handler.product.dto.CreateProduct
import com.kroffle.knitting.controller.handler.product.dto.EditProductContent
import com.kroffle.knitting.controller.handler.product.dto.EditProductPackage
import com.kroffle.knitting.controller.handler.product.dto.RegisterProduct
import com.kroffle.knitting.controller.handler.product.dto.UpdateProduct
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
    @Deprecated(message = "use DraftDesign")
    fun editProductPackage(request: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(request)
        val bodyMono = request.safetyBodyToMono(EditProductPackage.Request::class.java)

        val product: Mono<Product> = bodyMono
            .map { ProductRequestMapper.toEditProductPackageData(it, knitterId) }
            .flatMap(productService::edit)

        return product
            .doOnError(ExceptionHelper::raiseException)
            .map(ProductResponseMapper::toEditProductPackageResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    @Deprecated(message = "use DraftDesign")
    fun editProductContent(request: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(request)
        val bodyMono = request.safetyBodyToMono(EditProductContent.Request::class.java)

        val product: Mono<Product> = bodyMono
            .map { ProductRequestMapper.toEditProductContentData(it, knitterId) }
            .flatMap(productService::edit)

        return product
            .doOnError(ExceptionHelper::raiseException)
            .map(ProductResponseMapper::toEditProductContentResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    @Deprecated(message = "use createProduct")
    fun registerProduct(request: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(request)
        val bodyMono = request.safetyBodyToMono(RegisterProduct.Request::class.java)

        val product: Mono<Product> = bodyMono
            .map { ProductRequestMapper.toRegisterProductData(it, knitterId) }
            .flatMap(productService::register)

        return product
            .doOnError(ExceptionHelper::raiseException)
            .map(ProductResponseMapper::toRegisterProductResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun createProduct(request: ServerRequest): Mono<ServerResponse> {
        val product = request.safetyBodyToMono(CreateProduct.Request::class.java)
        val knitterId = AuthHelper.getKnitterId(request)
        return product
            .map { ProductRequestMapper.toCreateProductData(it, knitterId) }
            .flatMap(productService::create)
            .doOnError(ExceptionHelper::raiseException)
            .map(ProductResponseMapper::toCreateProductResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun updateProduct(request: ServerRequest): Mono<ServerResponse> {
        val product = request.safetyBodyToMono(UpdateProduct.Request::class.java)
        val productId = request.getLongPathVariable("productId")
        val knitterId = AuthHelper.getKnitterId(request)
        return product
            .map { ProductRequestMapper.toUpdateProductData(it, productId, knitterId) }
            .flatMap(productService::update)
            .doOnError(ExceptionHelper::raiseException)
            .map(ProductResponseMapper::toUpdateProductResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun getMyProduct(request: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getKnitterId(request)
        val productId = request.getLongPathVariable("productId")

        return productService
            .get(ProductRequestMapper.toGetMyProductData(productId, knitterId))
            .doOnError(ExceptionHelper::raiseException)
            .map(ProductResponseMapper::toGetMyProductResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun getMyProducts(request: ServerRequest): Mono<ServerResponse> {
        val paging = PaginationHelper.getPagingFromRequest(request)
        val knitterId = AuthHelper.getKnitterId(request)

        return productService
            .get(ProductRequestMapper.toGetMyProductsData(paging, knitterId))
            .doOnError(ExceptionHelper::raiseException)
            .map(ProductResponseMapper::toGetMyProductsResponse)
            .collect(toList())
            .flatMap(ResponseHelper::makeJsonResponse)
    }
}
