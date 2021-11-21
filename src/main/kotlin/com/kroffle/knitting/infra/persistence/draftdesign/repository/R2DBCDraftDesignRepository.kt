package com.kroffle.knitting.infra.persistence.draftdesign.repository

import com.kroffle.knitting.infra.persistence.draftdesign.entity.DraftDesignEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface R2DBCDraftDesignRepository : ReactiveCrudRepository<DraftDesignEntity, Long> {
    fun findByIdAndKnitterId(id: Long, knitterId: Long): Mono<DraftDesignEntity>
}
