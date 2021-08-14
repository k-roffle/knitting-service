package com.kroffle.knitting.usecase.design

import com.kroffle.knitting.domain.design.entity.Design
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class DesignService(private val repository: DesignRepository) {
    fun create(design: Design): Mono<Design> = repository.createDesign(design)

    fun getMyDesign(knitterId: Long): Flux<Design> =
        repository
            .getDesignsByKnitterId(knitterId)

    interface DesignRepository {
        fun createDesign(design: Design): Mono<Design>
        fun getDesignsByKnitterId(knitterId: Long): Flux<Design>
    }
}
