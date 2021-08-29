package com.kroffle.knitting.infra.persistence.knitter.repository

import com.kroffle.knitting.domain.knitter.entity.Knitter
import com.kroffle.knitting.infra.persistence.knitter.entity.toKnitterEntity
import com.kroffle.knitting.usecase.repository.KnitterRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class R2dbcKnitterRepository(private val repository: DBKnitterRepository) : KnitterRepository {
    override fun create(user: Knitter): Mono<Knitter> = repository
        .save(user.toKnitterEntity())
        .map { it.toKnitter() }

    override fun findByEmail(email: String): Mono<Knitter> = repository
        .findFirstByEmail(email)
        .map { it.toKnitter() }

    override fun findById(id: Long): Mono<Knitter> = repository
        .findById(id)
        .map { it.toKnitter() }
}
