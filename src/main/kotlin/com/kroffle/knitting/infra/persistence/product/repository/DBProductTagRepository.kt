package com.kroffle.knitting.infra.persistence.product.repository

import com.kroffle.knitting.infra.persistence.product.entity.ProductTagEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface DBProductTagRepository : ReactiveCrudRepository<ProductTagEntity, Long> {
    fun findAllByProductId(productId: Long): Flux<ProductTagEntity>
    fun findAllByProductIdIn(productId: List<Long>): Flux<ProductTagEntity>
}
