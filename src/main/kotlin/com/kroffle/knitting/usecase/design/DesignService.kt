package com.kroffle.knitting.usecase.design

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.usecase.design.dto.MyDesignFilter
import com.kroffle.knitting.usecase.helper.pagination.type.Paging
import com.kroffle.knitting.usecase.helper.pagination.type.Sort
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class DesignService(private val repository: DesignRepository) {
    fun create(design: Design): Mono<Design> = repository.createDesign(design)

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
