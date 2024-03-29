package com.kroffle.knitting.infra.persistence.draftdesign.repository

import com.kroffle.knitting.infra.persistence.draftdesign.entity.DraftDesignEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface R2DBCDraftDesignRepository : ReactiveCrudRepository<DraftDesignEntity, Long> {
    fun findByIdAndKnitterId(id: Long, knitterId: Long): Mono<DraftDesignEntity>
    fun findByKnitterIdAndDesignId(knitterId: Long, designId: Long?): Flux<DraftDesignEntity>
    fun getByKnitterIdAndDesignId(knitterId: Long, designId: Long): Mono<DraftDesignEntity>
}
