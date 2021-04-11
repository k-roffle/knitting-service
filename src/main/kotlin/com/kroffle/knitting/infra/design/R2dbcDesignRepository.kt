package com.kroffle.knitting.infra.design

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.infra.design.entity.toDesignEntity
import com.kroffle.knitting.usecase.design.DesignRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class R2dbcDesignRepository(private val dbDesignRepository: DBDesignRepository) : DesignRepository {
    override fun getAll(): Flux<Design> =
        dbDesignRepository
            .findAll()
            .map {
                it.toDesign()
            }

    override fun createDesign(design: Mono<Design>): Mono<Design> {
        return design
            .flatMap { dbDesignRepository.save(it.toDesignEntity()) }
            .map { it.toDesign() }
    }
}
