package com.kroffle.knitting.infra.persistence.design

import com.kroffle.knitting.infra.persistence.design.entity.DesignEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface DBDesignRepository : ReactiveCrudRepository<DesignEntity, Long> {
    fun findAllByKnitterId(knitterId: Long, pageable: Pageable): Flux<DesignEntity>
    fun findAllByKnitterIdAndIdBefore(knitterId: Long, id: Long, pageable: Pageable): Flux<DesignEntity>
}
