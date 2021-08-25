package com.kroffle.knitting.controller.handler.knitter.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload

data class SalesSummaryResponse(
    @JsonProperty("number_of_products_on_sales")
    val numberOfProductsOnSales: Int,
    @JsonProperty("number_of_products_sold")
    val numberOfProductsSold: Int,
) : ObjectPayload
