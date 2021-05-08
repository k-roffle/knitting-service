package com.kroffle.knitting.usecase.design

import com.kroffle.knitting.domain.design.entity.Design
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.server.ServerWebInputException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.stream.Collectors.toList

@Component
class DesignHandler(private val repository: DesignRepository) {

    private val validator = DesignValidator()

    fun getAll(req: ServerRequest): Mono<ServerResponse> =
        repository
            .getAll()
            .collect(toList())
            .flatMap {
                ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(it)
            }

    fun createDesign(req: ServerRequest): Mono<ServerResponse> {
        val design: Mono<Design> = req.bodyToMono(Design::class.java).doOnNext { validate(it) }
        return design
            .flatMap { repository.createDesign(it) }
            .flatMap {
                ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(it)
            }
    }

    interface DesignRepository {
        fun getAll(): Flux<Design>
        fun createDesign(design: Design): Mono<Design>
    }

    private fun validate(design: Design) {
        val errors: Errors = BeanPropertyBindingResult(design, "design")
        validator.validate(design, errors)
        if (errors.hasErrors()) {
            throw ServerWebInputException(errors.toString())
        }
    }
}
