package com.kroffle.knitting.controller.handler.design

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.usecase.design.DesignService
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.server.ServerWebInputException
import reactor.core.publisher.Mono
import java.util.stream.Collectors.toList

@Component
class DesignHandler(private val service: DesignService) {

    private val validator = DesignValidator()

    fun getAll(req: ServerRequest): Mono<ServerResponse> =
        service
            .getAll()
            .collect(toList())
            .flatMap {
                ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(it)
            }

    fun createDesign(req: ServerRequest): Mono<ServerResponse> {
        val design: Mono<Design> = req
            .bodyToMono(Design::class.java)
            .switchIfEmpty(Mono.error(ServerWebInputException("Body is required")))
            .doOnNext { validate(it) }
        return design
            .flatMap { service.create(it) }
            .flatMap {
                ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(it)
            }
    }

    private fun validate(design: Design) {
        val errors: Errors = BeanPropertyBindingResult(design, "design")
        validator.validate(design, errors)
        if (errors.hasErrors()) {
            throw ServerWebInputException(errors.toString())
        }
    }
}
