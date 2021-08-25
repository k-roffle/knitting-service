package com.kroffle.knitting.usecase.product

import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.usecase.product.dto.DraftProductContent
import com.kroffle.knitting.usecase.product.dto.DraftProductPackage
import com.kroffle.knitting.usecase.product.dto.RegisterProductData
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ProductService(private val repository: ProductRepository) {
    fun draft(product: DraftProductPackage): Mono<Product> =
        repository.save(
            Product.draftProductPackage(
                id = product.id,
                knitterId = product.knitterId,
                name = product.name,
                fullPrice = product.fullPrice,
                discountPrice = product.discountPrice,
                representativeImageUrl = product.representativeImageUrl,
                specifiedSalesStartDate = product.specifiedSalesStartDate,
                specifiedSalesEndDate = product.specifiedSalesEndDate,
                tags = product.tags,
                items = product.items,
            )
        )

    fun draft(product: DraftProductContent): Mono<Product> {
        return repository
            .getProductByIdAndKnitterId(product.id, product.knitterId)
            .flatMap {
                repository
                    .save(it.draftContent(product.content))
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
