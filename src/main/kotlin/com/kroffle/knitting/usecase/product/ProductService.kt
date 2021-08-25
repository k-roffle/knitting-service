package com.kroffle.knitting.usecase.product

import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.usecase.product.dto.DraftProductPackage
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

    interface ProductRepository {
        fun save(product: Product): Mono<Product>
    }
}
