package com.kroffle.knitting.infra.persistence.design.repository

import com.kroffle.knitting.infra.persistence.design.entity.TechniqueEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface DBTechniqueRepository : ReactiveCrudRepository<TechniqueEntity, Long> {
    fun deleteByDesignId(designId: Long): Mono<Long>
    fun findAllByDesignIdIn(designIds: List<Long>): Flux<TechniqueEntity>
}
