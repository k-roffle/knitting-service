package com.kroffle.knitting.usecase.design

import com.kroffle.knitting.domain.design.entity.Design
import reactor.core.publisher.Mono

class DesignService(private val repository: DesignRepository) {
    fun create(design: Design): Mono<Design> = repository.createDesign(design)

    interface DesignRepository {
        fun createDesign(design: Design): Mono<Design>
    }
}
