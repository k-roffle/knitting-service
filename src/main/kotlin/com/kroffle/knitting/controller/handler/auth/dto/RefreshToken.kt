package com.kroffle.knitting.controller.handler.auth.dto

import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload

object RefreshToken {
    data class Response(
        val token: String,
    ) : ObjectPayload
}
