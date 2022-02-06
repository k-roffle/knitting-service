package com.kroffle.knitting.controller.handler.product.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload
import java.time.LocalDate
import java.time.OffsetDateTime

object GetMyProduct {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class Response(
        val id: Long,
        val name: String,
        val fullPrice: Int,
        val discountPrice: Int,
        val representativeImageUrl: String,
        val specifiedSalesStartDate: LocalDate?,
        val specifiedSalesEndDate: LocalDate?,
        val tags: List<String>,
        val content: String?,
        val inputStatus: String,
        val itemIds: List<Long>,
        val createdAt: OffsetDateTime?,
        val updatedAt: OffsetDateTime?,
    ) : ObjectPayload
}
