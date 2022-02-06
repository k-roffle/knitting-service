package com.kroffle.knitting.controller.handler.product.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload
import java.time.OffsetDateTime

object CreateProduct {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class Request(
        val designIds: List<Long>,
        val name: String,
        val fullPrice: Int,
        val discountPrice: Int,
        val representativeImageUrl: String,
        val specifiedSalesStartedAt: OffsetDateTime?,
        val specifiedSalesEndedAt: OffsetDateTime?,
        val tags: List<String>,
        val content: String,
        val draftId: Long?,
    )

    data class Response(
        val id: Long,
    ) : ObjectPayload
}
