package com.kroffle.knitting.controller.handler.draftproduct.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload

object SaveDraftProduct {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class Request(
        val id: Long?,
        val productId: Long?,
        val value: String,
    )

    data class Response(
        val id: Long,
    ) : ObjectPayload
}
