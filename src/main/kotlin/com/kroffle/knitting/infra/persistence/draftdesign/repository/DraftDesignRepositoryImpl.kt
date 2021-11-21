package com.kroffle.knitting.infra.persistence.draftdesign.repository

import com.kroffle.knitting.domain.draftdesign.entity.DraftDesign
import com.kroffle.knitting.infra.persistence.draftdesign.entity.toDraftDesignEntity
import com.kroffle.knitting.infra.persistence.exception.NotFoundEntity
import com.kroffle.knitting.usecase.repository.DraftDesignRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class DraftDesignRepositoryImpl(
    private val draftDesignRepository: R2DBCDraftDesignRepository,
) : DraftDesignRepository {
    override fun findByIdAndKnitterId(id: Long, knitterId: Long): Mono<DraftDesign> =
        draftDesignRepository
            .findByIdAndKnitterId(id, knitterId)
            .switchIfEmpty(Mono.error(NotFoundEntity(DraftDesign::class.java)))
            .map { it.toDraftDesign() }

    override fun save(draftDesign: DraftDesign): Mono<DraftDesign> =
        draftDesignRepository
            .save(draftDesign.toDraftDesignEntity())
            .map { it.toDraftDesign() }
}
