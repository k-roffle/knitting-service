package com.kroffle.knitting.infra.persistence.knitter.repository

import com.kroffle.knitting.domain.knitter.entity.Knitter
import com.kroffle.knitting.infra.persistence.exception.NotFoundEntity
import com.kroffle.knitting.infra.persistence.knitter.entity.KnitterEntity
import com.kroffle.knitting.infra.persistence.knitter.entity.toKnitterEntity
import com.kroffle.knitting.usecase.repository.KnitterRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class KnitterRepositoryImpl(private val repository: R2DBCKnitterRepository) : KnitterRepository {
    override fun create(user: Knitter): Mono<Knitter> =
        repository
            .save(user.toKnitterEntity())
            .map { it.toKnitter() }

    override fun findByEmail(email: String): Mono<Knitter> =
        repository
            .findFirstByEmail(email)
            .map { it.toKnitter() }

    override fun getById(id: Long): Mono<Knitter> =
        findById(id)
            .switchIfEmpty(Mono.error(NotFoundEntity(KnitterEntity::class.java)))

    private fun findById(id: Long): Mono<Knitter> =
        repository
            .findById(id)
            .map { it.toKnitter() }
}
