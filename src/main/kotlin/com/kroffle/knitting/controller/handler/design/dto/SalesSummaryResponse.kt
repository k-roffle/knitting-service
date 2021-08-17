package com.kroffle.knitting.controller.handler.design.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectData

data class SalesSummaryResponse(
    @JsonProperty("number_of_designs_on_sales")
    val numberOfDesignsOnSales: Int,
    @JsonProperty("number_of_designs_sold")
    val numberOfDesignsSold: Int,
) : ObjectData