package com.kroffle.knitting.controller.handler.knitter.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload

object SalesSummary {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class Response(
        val numberOfProductsOnSales: Long,
        val numberOfProductsSold: Long,
    ) : ObjectPayload
}
