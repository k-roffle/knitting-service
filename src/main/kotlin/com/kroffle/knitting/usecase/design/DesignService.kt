package com.kroffle.knitting.usecase.design

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.draftdesign.entity.DraftDesign
import com.kroffle.knitting.usecase.design.dto.CreateDesignData
import com.kroffle.knitting.usecase.design.dto.MyDesignFilter
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
        designRepository
            .createDesign(
                Design.new(
                    knitterId = data.knitterId,
                    name = data.name,
                    designType = data.designType,
                    patternType = data.patternType,
                    gauge = data.gauge,
                    size = data.size,
                    needle = data.needle,
                    yarn = data.yarn,
                    extra = data.extra,
                    price = data.price,
                    pattern = data.pattern,
                    description = data.description,
                    targetLevel = data.targetLevel,
                    coverImageUrl = data.coverImageUrl,
                    techniques = data.techniques,
                )
            )

    fun create(data: CreateDesignData): Mono<Design> {
        return if (data.draftId == null) {
            createDesign(data)
        } else {
            createDesign(data)
                .flatMap { design ->
                    draftDesignRepository
                        .getDraftDesign(
                            id = data.draftId,
                            knitterId = data.knitterId,
                        )
                        .flatMap {
                            draftDesignRepository
                                .delete(it)
                                .map { design }
                        }
                }
        }
    }

    fun getMyDesign(filter: MyDesignFilter): Flux<Design> =
        designRepository
            .getDesignsByKnitterId(
                filter.knitterId,
                filter.paging,
                filter.sort,
            )

    interface DesignRepository {
        fun createDesign(design: Design): Mono<Design>
        fun getDesignsByKnitterId(knitterId: Long, paging: Paging, sort: Sort): Flux<Design>
    }

    interface DraftDesignRepository {
        fun getDraftDesign(id: Long, knitterId: Long): Mono<DraftDesign>
        fun delete(draftDesign: DraftDesign): Mono<Long>
    }
}
