package com.kroffle.knitting.controller.handler.draftproduct.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload

object SaveDraftProduct {
    data class Request(
        val id: Long?,
        @JsonProperty("product_id")
        val productId: Long?,
        val value: String,
    )

    data class Response(
        val id: Long,
    ) : ObjectPayload
}
