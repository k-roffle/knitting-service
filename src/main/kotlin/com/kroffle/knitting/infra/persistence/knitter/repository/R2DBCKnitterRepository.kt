package com.kroffle.knitting.infra.persistence.knitter.repository

import com.kroffle.knitting.infra.persistence.knitter.entity.KnitterEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface R2DBCKnitterRepository : ReactiveCrudRepository<KnitterEntity, Long> {
    fun findFirstByEmail(email: String): Mono<KnitterEntity>
}
