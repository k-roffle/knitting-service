package com.kroffle.knitting.usecase.product

import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.usecase.product.dto.DraftProductContentData
import com.kroffle.knitting.usecase.product.dto.DraftProductPackageData
import com.kroffle.knitting.usecase.product.dto.RegisterProductData
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ProductService(private val repository: ProductRepository) {
    fun draft(data: DraftProductPackageData): Mono<Product> =
        repository.save(
            Product.draftProductPackage(
                id = data.id,
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

    fun draft(data: DraftProductContentData): Mono<Product> {
        return repository
            .getProductByIdAndKnitterId(data.id, data.knitterId)
            .flatMap {
                repository
                    .save(it.draftContent(data.content))
            }
    }

    fun register(data: RegisterProductData): Mono<Product> {
        return repository
            .getProductByIdAndKnitterId(data.id, data.knitterId)
            .flatMap {
                repository
                    .save(it.register())
            }
    }

    interface ProductRepository {
        fun save(product: Product): Mono<Product>
        fun getProductByIdAndKnitterId(id: Long, knitterId: Long): Mono<Product>
    }
}
