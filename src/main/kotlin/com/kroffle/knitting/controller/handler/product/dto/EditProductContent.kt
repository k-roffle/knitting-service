package com.kroffle.knitting.controller.handler.product.dto

import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload

object EditProductContent {
    data class Request(
        val id: Long,
        val content: String,
    )

    data class Response(
        val id: Long,
    ) : ObjectPayload
}
