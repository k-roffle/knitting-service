package com.kroffle.knitting.controller.handler.knitter.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload

data class SalesSummaryResponse(
    @JsonProperty("number_of_designs_on_sales")
    val numberOfDesignsOnSales: Int,
    @JsonProperty("number_of_designs_sold")
    val numberOfDesignsSold: Int,
) : ObjectPayload
