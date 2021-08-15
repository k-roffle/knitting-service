package com.kroffle.knitting.infra.persistence.design

import com.kroffle.knitting.infra.persistence.design.entity.DesignEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface DBDesignRepository : ReactiveCrudRepository<DesignEntity, Long> {
    fun findAllByKnitterId(knitterId: Long): Flux<DesignEntity>
}
