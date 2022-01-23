package com.kroffle.knitting.usecase.draftdesign

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.draftdesign.entity.DraftDesign
import com.kroffle.knitting.usecase.draftdesign.dto.SaveDraftDesignData
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class DraftDesignService(
    private val draftDesignRepository: DraftDesignRepository,
    private val designRepository: DesignRepository,
) {
    private fun verifyDesignId(designId: Long, knitterId: Long): Mono<Design> {
        return designRepository.getDesign(designId, knitterId)
    }

    private fun saveDraftDesign(data: SaveDraftDesignData): Mono<DraftDesign> {
        return if (data.id == null) {
            draftDesignRepository.create(
                DraftDesign.new(
                    knitterId = data.knitterId,
                    designId = data.designId,
                    value = data.value,
                )
            )
        } else {
            draftDesignRepository
                .getDraftDesign(data.id, data.knitterId)
                .map { it.merge(data.value) }
                .flatMap {
                    draftDesignRepository.update(it)
                }
        }
    }

    fun saveDraft(data: SaveDraftDesignData): Mono<DraftDesign> {
        return if (data.designId == null) {
            saveDraftDesign(data)
        } else {
            verifyDesignId(data.designId, data.knitterId)
                .flatMap { saveDraftDesign(data) }
        }
    }

    fun getMyDraftDesigns(knitterId: Long): Flux<DraftDesign> =
        draftDesignRepository.findDraftDesignsToCreate(knitterId)

    fun getMyDraftDesign(draftDesignId: Long, knitterId: Long): Mono<DraftDesign> =
        draftDesignRepository.getDraftDesign(
            id = draftDesignId,
            knitterId = knitterId,
        )

    fun getMyDraftDesignToUpdate(designId: Long, knitterId: Long): Mono<DraftDesign> =
        draftDesignRepository.getDraftDesignToUpdate(
            designId = designId,
            knitterId = knitterId,
        )

    fun deleteMyDraftDesign(draftDesignId: Long, knitterId: Long): Mono<Long> {
        val draftDesign = draftDesignRepository
            .getDraftDesign(draftDesignId, knitterId)
        return draftDesign
            .flatMap { draftDesignRepository.delete(it) }
    }

    interface DraftDesignRepository {
        fun getDraftDesign(id: Long, knitterId: Long): Mono<DraftDesign>
        fun findDraftDesignsToCreate(knitterId: Long): Flux<DraftDesign>
        fun getDraftDesignToUpdate(designId: Long, knitterId: Long): Mono<DraftDesign>
        fun update(draftDesign: DraftDesign): Mono<DraftDesign>
        fun create(draftDesign: DraftDesign): Mono<DraftDesign>
        fun delete(draftDesign: DraftDesign): Mono<Long>
    }

    interface DesignRepository {
        fun getDesign(id: Long, knitterId: Long): Mono<Design>
    }
}
