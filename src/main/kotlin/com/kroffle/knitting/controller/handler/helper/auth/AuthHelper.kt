package com.kroffle.knitting.controller.handler.helper.auth

import com.kroffle.knitting.controller.handler.exception.Unauthorized
import org.springframework.web.reactive.function.server.ServerRequest

class AuthHelper {
    companion object {
        fun getAuthenticatedId(request: ServerRequest): Long {
            val userId = request.attribute("userId")
            if (userId.isEmpty) {
                throw Unauthorized("userId is required")
            }
            return userId.get() as Long
        }
    }
}
