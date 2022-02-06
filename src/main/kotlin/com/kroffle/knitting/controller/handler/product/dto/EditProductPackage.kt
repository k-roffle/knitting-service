package com.kroffle.knitting.controller.handler.product.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload
import java.time.LocalDate

object EditProductPackage {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class Request(
        val id: Long?,
        val name: String,
        val fullPrice: Int,
        val discountPrice: Int,
        val representativeImageUrl: String,
        val specifiedSalesStartDate: LocalDate?,
        val specifiedSalesEndDate: LocalDate?,
        val tags: List<String>,
        val designIds: List<Long>,
    )

    data class Response(
        val id: Long,
    ) : ObjectPayload
}
