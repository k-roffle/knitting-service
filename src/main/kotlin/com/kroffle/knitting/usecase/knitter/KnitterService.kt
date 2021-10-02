package com.kroffle.knitting.usecase.knitter

import com.kroffle.knitting.domain.knitter.entity.Knitter
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class KnitterService(private val knitterRepository: KnitterRepository) {
    fun getKnitter(knitterId: Long): Mono<Knitter> =
        knitterRepository.getById(knitterId)

    interface KnitterRepository {
        fun getById(id: Long): Mono<Knitter>
    }
}
