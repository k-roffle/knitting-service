package com.kroffle.knitting.usecase.design

import com.kroffle.knitting.domain.design.entity.Design
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.stream.Collectors.toList

@Component
class DesignHandler(private val repository: DesignRepository) {
    fun getAll(req: ServerRequest): Mono<ServerResponse> =
        repository
            .getAll()
            .collect(toList())
            .flatMap {
                ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(it)
            }
    interface DesignRepository {
        fun getAll(): Flux<Design>
    }
}
