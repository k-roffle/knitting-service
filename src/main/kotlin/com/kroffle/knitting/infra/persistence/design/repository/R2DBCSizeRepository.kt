package com.kroffle.knitting.infra.persistence.design.repository

import com.kroffle.knitting.infra.persistence.design.entity.SizeEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface R2DBCSizeRepository : ReactiveCrudRepository<SizeEntity, Long> {
    fun deleteByDesignId(designId: Long): Mono<Long>
    fun findAllByDesignIdIn(designIds: List<Long>): Flux<SizeEntity>
    fun findByDesignId(designId: Long): Mono<SizeEntity>
}
