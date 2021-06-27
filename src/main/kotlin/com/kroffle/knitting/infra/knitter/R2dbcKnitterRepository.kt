package com.kroffle.knitting.infra.knitter

import com.kroffle.knitting.domain.knitter.entity.Knitter
import com.kroffle.knitting.infra.knitter.entity.toKnitterEntity
import com.kroffle.knitting.usecase.auth.AuthService
import reactor.core.publisher.Mono

class R2dbcKnitterRepository(private val dbDesignRepository: DBKnitterRepository) : AuthService.KnitterRepository {
    override fun create(user: Knitter): Mono<Knitter> = dbDesignRepository
        .save(user.toKnitterEntity())
        .map { it.toKnitter() }

    override fun findByEmail(email: String): Mono<Knitter> = dbDesignRepository
        .findFirstByEmail(email)
        .map { it.toKnitter() }
}
