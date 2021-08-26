package com.kroffle.knitting.infra.persistence.product.repository

import com.kroffle.knitting.infra.persistence.product.entity.ProductItemEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface DBProductItemRepository : ReactiveCrudRepository<ProductItemEntity, Long> {
    fun findAllByProductId(productId: Long): Flux<ProductItemEntity>
    fun findAllByProductIdIn(productId: List<Long>): Flux<ProductItemEntity>
}
