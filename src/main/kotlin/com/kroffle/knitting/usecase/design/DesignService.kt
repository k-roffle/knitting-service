package com.kroffle.knitting.usecase.design

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.usecase.design.dto.CreateDesignData
import com.kroffle.knitting.usecase.design.dto.MyDesignFilter
import com.kroffle.knitting.usecase.helper.pagination.type.Paging
import com.kroffle.knitting.usecase.helper.pagination.type.Sort
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class DesignService(private val repository: DesignRepository) {
    fun create(data: CreateDesignData): Mono<Design> =
        repository
            .createDesign(
                Design.new(
                    data.knitterId,
                    data.name,
                    data.designType,
                    data.patternType,
                    data.gauge,
                    data.size,
                    data.needle,
                    data.yarn,
                    data.extra,
                    data.pattern,
                    data.description,
                    data.targetLevel,
                    data.coverImageUrl,
                    data.techniques,
                )
            )

    fun getMyDesign(filter: MyDesignFilter): Flux<Design> =
        repository
            .getDesignsByKnitterId(
                filter.knitterId,
                filter.paging,
                filter.sort,
            )

    interface DesignRepository {
        fun createDesign(design: Design): Mono<Design>
        fun getDesignsByKnitterId(knitterId: Long, paging: Paging, sort: Sort): Flux<Design>
    }
}
