package com.kroffle.knitting.controller.handler.helper.auth

import com.kroffle.knitting.controller.handler.helper.auth.exception.KnitterIdRequired
import org.springframework.web.reactive.function.server.ServerRequest

class AuthHelper {
    companion object {
        fun getKnitterId(request: ServerRequest): Long {
            val knitterId = request.attribute("knitterId")
            if (knitterId.isEmpty) {
                throw KnitterIdRequired()
            }
            return knitterId.get() as Long
        }
    }
}
