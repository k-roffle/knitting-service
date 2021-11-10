package com.kroffle.knitting.infra.persistence.product.repository

import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.infra.persistence.product.entity.ProductEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface DBProductRepository : ReactiveCrudRepository<ProductEntity, Long> {
    fun findAllByKnitterIdAndInputStatus(knitterId: Long, inputStatus: Product.InputStatus): Flux<ProductEntity>
    fun findAllByKnitterIdAndIdBefore(knitterId: Long, id: Long, pageable: Pageable): Flux<ProductEntity>
    fun findAllByKnitterId(knitterId: Long, pageable: Pageable): Flux<ProductEntity>
}
