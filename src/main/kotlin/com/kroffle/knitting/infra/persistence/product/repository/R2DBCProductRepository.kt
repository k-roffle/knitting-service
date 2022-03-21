package com.kroffle.knitting.infra.persistence.product.repository

import com.kroffle.knitting.infra.persistence.product.entity.ProductEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface R2DBCProductRepository : ReactiveCrudRepository<ProductEntity, Long> {
    fun findAllByKnitterIdAndIdBefore(knitterId: Long, id: Long, pageable: Pageable): Flux<ProductEntity>
    fun findAllByKnitterId(knitterId: Long, pageable: Pageable): Flux<ProductEntity>
    fun countByKnitterId(knitterId: Long): Mono<Int>
}
