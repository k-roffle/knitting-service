package com.kroffle.knitting.usecase.product

import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.usecase.helper.pagination.type.Paging
import com.kroffle.knitting.usecase.helper.pagination.type.Sort
import com.kroffle.knitting.usecase.product.dto.EditProductContentData
import com.kroffle.knitting.usecase.product.dto.EditProductPackageData
import com.kroffle.knitting.usecase.product.dto.GetMyProductData
import com.kroffle.knitting.usecase.product.dto.GetMyProductsData
import com.kroffle.knitting.usecase.product.dto.RegisterProductData
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ProductService(private val repository: ProductRepository) {
    fun edit(data: EditProductPackageData): Mono<Product> =
        if (data.id == null) {
            repository.save(
                Product.create(
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
                    val updatedProduct = it.edit(
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

    fun edit(data: EditProductContentData): Mono<Product> {
        return repository
            .getProductByIdAndKnitterId(data.id, data.knitterId)
            .flatMap { repository.save(it.edit(data.content)) }
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
