package com.kroffle.knitting.controller.handler.knitter.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload

object SalesSummary {
    data class Response(
        @JsonProperty("number_of_products_on_sales")
        val numberOfProductsOnSales: Long,
        @JsonProperty("number_of_products_sold")
        val numberOfProductsSold: Long,
    ) : ObjectPayload
}
