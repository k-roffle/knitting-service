package com.kroffle.knitting.controller.handler.design

import com.kroffle.knitting.controller.handler.design.dto.MyDesign
import com.kroffle.knitting.controller.handler.design.dto.MyDesignsResponse
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

    fun getMyDesigns(req: ServerRequest): Mono<ServerResponse> {
        return ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                MyDesignsResponse(
                    designs = listOf(
                        MyDesign(
                            name = "캔디리더 효정 니트",
                            yarn = "패션아란 400g 1볼",
                            tags = listOf("니트", "서술형도안"),
                        ),
                        MyDesign(
                            name = "유샤샤 니트",
                            yarn = "캐시미어 300g 1볼",
                            tags = listOf("니트", "서술형도안"),
                        ),
                    )
                )
            )
    }

    private fun validate(design: Design) {
        val errors: Errors = BeanPropertyBindingResult(design, "design")
        validator.validate(design, errors)
        if (errors.hasErrors()) {
            throw ServerWebInputException(errors.toString())
        }
    }
}
