package com.kroffle.knitting.infra.design

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.infra.design.entity.toDesignEntity
import com.kroffle.knitting.usecase.design.DesignRepository
import reactor.core.publisher.Mono

class R2dbcDesignRepository(private val dbDesignRepository: DBDesignRepository) : DesignRepository {
    override fun createDesign(design: Design): Mono<Design> =
        dbDesignRepository
            .save(design.toDesignEntity())
            .map { it.toDesign() }
}
