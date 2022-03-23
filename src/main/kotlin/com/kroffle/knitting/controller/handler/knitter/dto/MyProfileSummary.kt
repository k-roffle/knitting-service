package com.kroffle.knitting.controller.handler.knitter.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload

object MyProfileSummary {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class Response(
        val myDesignsCount: Int,
        val myProductsCount: Int,
        val purchasedProductsCount: Int,
    ) : ObjectPayload
}
