package com.kroffle.knitting.infra.persistence.design.repository

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.infra.persistence.design.entity.DesignEntity
import com.kroffle.knitting.infra.persistence.design.entity.toDesignEntity
import com.kroffle.knitting.infra.persistence.helper.pagination.PaginationHelper
import com.kroffle.knitting.usecase.repository.DesignRepository
import com.kroffle.knitting.usecase.helper.pagination.type.Paging
import com.kroffle.knitting.usecase.helper.pagination.type.Sort
import com.kroffle.knitting.usecase.helper.pagination.type.SortDirection
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class R2dbcDesignRepository(private val repository: DBDesignRepository) : DesignRepository {
    override fun createDesign(design: Design): Mono<Design> =
        repository
            .save(design.toDesignEntity())
            .map { it.toDesign() }

    override fun getDesignsByKnitterId(knitterId: Long, paging: Paging, sort: Sort): Flux<Design> {
        val pageRequest = PaginationHelper.makePageRequest(paging, sort)

        val designs: Flux<DesignEntity> = when (sort.direction) {
            SortDirection.DESC ->
                if (paging.after != null) {
                    repository
                        .findAllByKnitterIdAndIdBefore(
                            knitterId = knitterId,
                            id = paging.after,
                            pageable = pageRequest,
                        )
                } else {
                    repository
                        .findAllByKnitterId(
                            knitterId = knitterId,
                            pageable = pageRequest,
                        )
                }
            else -> throw NotImplementedError()
        }
        return designs.map { it.toDesign() }
    }
}
