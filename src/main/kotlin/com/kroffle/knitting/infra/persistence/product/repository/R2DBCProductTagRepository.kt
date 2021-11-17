package com.kroffle.knitting.infra.persistence.product.repository

import com.kroffle.knitting.infra.persistence.product.entity.ProductTagEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface R2DBCProductTagRepository : ReactiveCrudRepository<ProductTagEntity, Long> {
    fun findAllByProductId(productId: Long): Flux<ProductTagEntity>
    fun findAllByProductIdIn(productId: List<Long>): Flux<ProductTagEntity>
    fun deleteByProductId(productId: Long): Mono<Long>
}
