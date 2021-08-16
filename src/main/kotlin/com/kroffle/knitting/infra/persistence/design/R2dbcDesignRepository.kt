package com.kroffle.knitting.infra.persistence.design

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.infra.persistence.design.entity.toDesignEntity
import com.kroffle.knitting.infra.persistence.helper.pagination.PaginationHelper
import com.kroffle.knitting.usecase.design.DesignRepository
import com.kroffle.knitting.usecase.helper.pagination.type.Paging
import com.kroffle.knitting.usecase.helper.pagination.type.Sort
import com.kroffle.knitting.usecase.helper.pagination.type.SortDirection
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class R2dbcDesignRepository(private val dbDesignRepository: DBDesignRepository) : DesignRepository {
    override fun createDesign(design: Design): Mono<Design> =
        dbDesignRepository
            .save(design.toDesignEntity())
            .map { it.toDesign() }

    override fun getDesignsByKnitterId(knitterId: Long, paging: Paging, sort: Sort): Flux<Design> {
        return when (sort.direction) {
            SortDirection.DESC ->
                dbDesignRepository
                    .findAllByKnitterIdAndIdBefore(
                        knitterId = knitterId,
                        id = paging.after,
                        pageable = PaginationHelper.makePageRequest(paging, sort)
                    )
                    .map { it.toDesign() }
            else -> throw NotImplementedError()
        }
    }
}
