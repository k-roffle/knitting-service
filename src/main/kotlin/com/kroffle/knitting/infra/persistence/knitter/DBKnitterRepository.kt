package com.kroffle.knitting.infra.persistence.knitter

import com.kroffle.knitting.infra.persistence.knitter.entity.KnitterEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface DBKnitterRepository : ReactiveCrudRepository<KnitterEntity, Long> {
    fun findFirstByEmail(email: String): Mono<KnitterEntity>
}
