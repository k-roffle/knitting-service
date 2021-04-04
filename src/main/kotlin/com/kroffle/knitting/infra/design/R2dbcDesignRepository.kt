package com.kroffle.knitting.infra.design

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.usecase.design.DesignRepository
import reactor.core.publisher.Flux

class R2dbcDesignRepository(private val dbDesignRepository: DBDesignRepository) : DesignRepository {
    override fun getAll(): Flux<Design> =
        dbDesignRepository
            .findAll()
            .map {
                it.toDesign()
            }
}
