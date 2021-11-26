package com.kroffle.knitting.usecase.draftdesign

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.draftdesign.entity.DraftDesign
import com.kroffle.knitting.usecase.draftdesign.dto.SaveDraftDesignData
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class DraftDesignService(
    private val draftDesignRepository: DraftDesignRepository,
    private val designRepository: DesignRepository,
) {
    private fun verifyDesignId(designId: Long, knitterId: Long): Mono<Design> {
        return designRepository
            .findByIdAndKnitterId(designId, knitterId)
    }

    private fun saveDraftDesign(data: SaveDraftDesignData): Mono<DraftDesign> {
        return if (data.id == null) {
            draftDesignRepository.save(
                DraftDesign.new(
                    knitterId = data.knitterId,
                    designId = data.designId,
                    value = data.value,
                )
            )
        } else {
            draftDesignRepository
                .findByIdAndKnitterId(data.id, data.knitterId)
                .map { it.merge(data.value) }
                .flatMap {
                    draftDesignRepository.save(it)
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

    interface DraftDesignRepository {
        fun findByIdAndKnitterId(id: Long, knitterId: Long): Mono<DraftDesign>
        fun save(draftDesign: DraftDesign): Mono<DraftDesign>
    }

    interface DesignRepository {
        fun findByIdAndKnitterId(id: Long, knitterId: Long): Mono<Design>
    }
}
