package com.kroffle.knitting.infra.persistence.draftproduct.repository

import com.kroffle.knitting.infra.persistence.draftproduct.entity.DraftProductEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface R2DBCDraftProductRepository : ReactiveCrudRepository<DraftProductEntity, Long> {
    fun findByIdAndKnitterId(id: Long, knitterId: Long): Mono<DraftProductEntity>
    fun findByKnitterIdAndProductId(knitterId: Long, productId: Long?): Flux<DraftProductEntity>
    fun getByKnitterIdAndProductId(knitterId: Long, productId: Long): Mono<DraftProductEntity>
}
