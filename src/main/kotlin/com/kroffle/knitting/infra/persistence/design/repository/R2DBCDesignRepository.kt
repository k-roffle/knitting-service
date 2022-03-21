package com.kroffle.knitting.infra.persistence.design.repository

import com.kroffle.knitting.infra.persistence.design.entity.DesignEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface R2DBCDesignRepository : ReactiveCrudRepository<DesignEntity, Long> {
    fun findByIdAndKnitterId(id: Long, knitterId: Long): Mono<DesignEntity>
    fun findAllByKnitterId(knitterId: Long, pageable: Pageable): Flux<DesignEntity>
    fun findAllByKnitterIdAndIdBefore(knitterId: Long, id: Long, pageable: Pageable): Flux<DesignEntity>
    fun countByKnitterId(knitterId: Long): Mono<Int>
}
