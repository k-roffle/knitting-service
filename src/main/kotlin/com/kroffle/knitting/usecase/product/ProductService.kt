package com.kroffle.knitting.usecase.product

import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.usecase.helper.pagination.type.Paging
import com.kroffle.knitting.usecase.helper.pagination.type.Sort
import com.kroffle.knitting.usecase.product.dto.DraftProductContentData
import com.kroffle.knitting.usecase.product.dto.DraftProductPackageData
import com.kroffle.knitting.usecase.product.dto.GetMyProductData
import com.kroffle.knitting.usecase.product.dto.GetMyProductsData
import com.kroffle.knitting.usecase.product.dto.RegisterProductData
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class ProductService(private val repository: ProductRepository) {
    fun draft(data: DraftProductPackageData): Mono<Product> =
        if (data.id == null) {
            repository.save(
                Product.draftProductPackage(
                    knitterId = data.knitterId,
                    name = data.name,
                    fullPrice = data.fullPrice,
                    discountPrice = data.discountPrice,
                    representativeImageUrl = data.representativeImageUrl,
                    specifiedSalesStartDate = data.specifiedSalesStartDate,
                    specifiedSalesEndDate = data.specifiedSalesEndDate,
                    tags = data.tags,
                    items = data.items,
                )
            )
        } else {
            repository
                .getProductByIdAndKnitterId(data.id, data.knitterId)
                .flatMap {
                    val updatedProduct = it.draftPackage(
                        knitterId = data.knitterId,
                        name = data.name,
                        fullPrice = data.fullPrice,
                        discountPrice = data.discountPrice,
                        representativeImageUrl = data.representativeImageUrl,
                        specifiedSalesStartDate = data.specifiedSalesStartDate,
                        specifiedSalesEndDate = data.specifiedSalesEndDate,
                        tags = data.tags,
                        items = data.items,
                    )
                    repository.save(updatedProduct)
                }
        }

    fun draft(data: DraftProductContentData): Mono<Product> {
        return repository
            .getProductByIdAndKnitterId(data.id, data.knitterId)
            .flatMap { repository.save(it.draftContent(data.content)) }
    }

    fun register(data: RegisterProductData): Mono<Product> {
        return repository
            .getProductByIdAndKnitterId(data.id, data.knitterId)
            .flatMap { repository.save(it.register()) }
    }

    fun get(data: GetMyProductData): Mono<Product> =
        repository
            .getProductByIdAndKnitterId(data.id, data.knitterId)

    fun get(data: GetMyProductsData): Flux<Product> =
        repository
            .getProductsByKnitterId(data.knitterId, data.paging, data.sort)

    interface ProductRepository {
        fun save(product: Product): Mono<Product>
        fun getProductByIdAndKnitterId(id: Long, knitterId: Long): Mono<Product>
        fun getProductsByKnitterId(knitterId: Long, paging: Paging, sort: Sort): Flux<Product>
    }
}
