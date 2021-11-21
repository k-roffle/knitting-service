package com.kroffle.knitting.infra.persistence.product.repository

import com.kroffle.knitting.infra.persistence.product.entity.ProductItemEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface R2DBCProductItemRepository : ReactiveCrudRepository<ProductItemEntity, Long> {
    fun findAllByProductId(productId: Long): Flux<ProductItemEntity>
    fun findAllByProductIdIn(productId: List<Long>): Flux<ProductItemEntity>
    fun deleteByProductId(productId: Long): Mono<Long>
}
