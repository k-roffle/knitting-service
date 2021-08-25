package com.kroffle.knitting.infra.persistence.product.repository

import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.infra.persistence.exception.NotFoundEntity
import com.kroffle.knitting.infra.persistence.product.entity.ProductEntity
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
    private fun findById(id: Long): Mono<Product> {
        val tags = productTagRepository
            .findAllByProductId(id)
            .map { it.toTag() }
            .collect(toList())

        val items = productItemRepository
            .findAllByProductId(id)
            .map { it.toItem() }
            .collect(toList())

        val product = productRepository
            .findById(id)

        return Mono.zip(product, tags, items)
            .map {
                it.t1.toProduct(it.t2, it.t3)
            }
    }

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

    override fun getProductByIdAndKnitterId(id: Long, knitterId: Long): Mono<Product> =
        findById(id)
            .filter {
                it.knitterId == knitterId
            }
            .switchIfEmpty(Mono.error(NotFoundEntity(ProductEntity::class.java)))
}
