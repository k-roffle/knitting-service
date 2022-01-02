package com.kroffle.knitting.controller.handler.helper.extension

import com.kroffle.knitting.controller.handler.exception.EmptyBodyException
import com.kroffle.knitting.controller.handler.exception.InvalidBodyException
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Mono

object ServerRequestExtension {
    fun <T> ServerRequest.safetyBodyToMono(clazz: Class<T>): Mono<T> =
        this
            .bodyToMono(clazz)
            .onErrorResume { Mono.error(InvalidBodyException()) }
            .switchIfEmpty(Mono.error(EmptyBodyException()))

    fun ServerRequest.getLongPathVariable(key: String): Long =
        this.pathVariable(key).toLong()
}
