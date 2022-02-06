package com.kroffle.knitting.usecase.design

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.draftdesign.entity.DraftDesign
import com.kroffle.knitting.usecase.design.dto.CreateDesignData
import com.kroffle.knitting.usecase.design.dto.GetMyDesignData
import com.kroffle.knitting.usecase.design.dto.MyDesignFilter
import com.kroffle.knitting.usecase.design.dto.UpdateDesignData
import com.kroffle.knitting.usecase.helper.pagination.type.Paging
import com.kroffle.knitting.usecase.helper.pagination.type.Sort
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class DesignService(
    private val designRepository: DesignRepository,
    private val draftDesignRepository: DraftDesignRepository,
) {
    private fun createDesign(data: CreateDesignData): Mono<Design> =
        with(data) {
            designRepository
                .createDesign(
                    Design.new(
                        knitterId = knitterId,
                        name = name,
                        designType = designType,
                        patternType = patternType,
                        gauge = gauge,
                        size = size,
                        needle = needle,
                        yarn = yarn,
                        extra = extra,
                        price = price,
                        pattern = pattern,
                        description = description,
                        targetLevel = targetLevel,
                        coverImageUrl = coverImageUrl,
                        techniques = techniques,
                    )
                )
        }

    private fun updateDesign(data: UpdateDesignData): Mono<Design> =
        with(data) {
            designRepository
                .getDesign(data.id, data.knitterId)
                .flatMap { design ->
                    designRepository.updateDesign(
                        design.update(
                            designType = designType,
                            patternType = patternType,
                            gauge = gauge,
                            size = size,
                            needle = needle,
                            yarn = yarn,
                            extra = extra,
                            pattern = pattern,
                            description = description,
                            targetLevel = targetLevel,
                            techniques = techniques,
                        )
                    )
                }
        }

    private fun deleteMyDraftDesign(draftId: Long?, knitterId: Long): Mono<Long> {
        if (draftId == null) return Mono.empty()
        return draftDesignRepository
            .getDraftDesign(
                id = draftId,
                knitterId = knitterId,
            )
            .flatMap {
                draftDesignRepository
                    .delete(it)
            }
    }

    fun create(data: CreateDesignData): Mono<Design> =
        createDesign(data)
            .flatMap { design ->
                deleteMyDraftDesign(data.draftId, data.knitterId)
                    .map { design }
                    .defaultIfEmpty(design)
            }

    fun update(data: UpdateDesignData): Mono<Design> {
        return updateDesign(data)
            .flatMap { design ->
                deleteMyDraftDesign(data.draftId, data.knitterId)
                    .map { design }
                    .defaultIfEmpty(design)
            }
    }

    fun getMyDesign(data: GetMyDesignData): Mono<Design> =
        with(data) {
            designRepository.getDesign(id, knitterId)
        }

    fun getMyDesigns(filter: MyDesignFilter): Flux<Design> =
        with(filter) {
            designRepository.getDesignsByKnitterId(knitterId, paging, sort)
        }

    interface DesignRepository {
        fun getDesign(id: Long, knitterId: Long): Mono<Design>
        fun createDesign(design: Design): Mono<Design>
        fun updateDesign(design: Design): Mono<Design>
        fun getDesignsByKnitterId(knitterId: Long, paging: Paging, sort: Sort): Flux<Design>
    }

    interface DraftDesignRepository {
        fun getDraftDesign(id: Long, knitterId: Long): Mono<DraftDesign>
        fun delete(draftDesign: DraftDesign): Mono<Long>
    }
}
