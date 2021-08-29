package com.kroffle.knitting.infra.persistence.product.repository

import com.kroffle.knitting.domain.product.enum.InputStatus
import com.kroffle.knitting.infra.persistence.product.entity.ProductEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface DBProductRepository : ReactiveCrudRepository<ProductEntity, Long> {
    fun findAllByKnitterIdAndInputStatus(knitterId: Long, inputStatus: InputStatus): Flux<ProductEntity>
}
