package com.kroffle.knitting.controller.handler.product.dto

import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload

object RegisterProduct {
    data class Request(
        val id: Long,
    )

    data class Response(
        val id: Long,
    ) : ObjectPayload
}
