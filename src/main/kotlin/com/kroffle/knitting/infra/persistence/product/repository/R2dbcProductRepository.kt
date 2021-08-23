package com.kroffle.knitting.infra.persistence.product.repository

import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.infra.persistence.product.entity.toProductEntity
import com.kroffle.knitting.infra.persistence.product.entity.toProductItemEntities
import com.kroffle.knitting.infra.persistence.product.entity.toProductTagEntities
import com.kroffle.knitting.usecase.product.ProductRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.stream.Collectors.toList

@Component
class R2dbcProductRepository(
    private val productRepository: DBProductRepository,
    private val productTagRepository: DBProductTagRepository,
    private val productItemRepository: DBProductItemRepository,
) : ProductRepository {
    override fun save(product: Product): Mono<Product> {
        return productRepository
            .save(product.toProductEntity())
            .flatMap {
                productEntity ->
                val productId: Long = productEntity.getNotNullId()
                val tags = productTagRepository
                    .saveAll(product.toProductTagEntities(productId))
                    .map { it.toTag() }
                    .collect(toList())

                val items = productItemRepository
                    .saveAll(product.toProductItemEntities(productId))
                    .map { it.toItem() }
                    .collect(toList())

                Mono.zip(tags, items)
                    .map {
                        productEntity.toProduct(it.t1, it.t2)
                    }
            }
    }
}
