package com.kroffle.knitting.controller.handler.product.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload
import java.time.OffsetDateTime

// TODO: 수정 가능한 범위 정하기 with 해선
object UpdateProduct {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class Request(
        val discountPrice: Int,
        val specifiedSalesStartDate: OffsetDateTime?,
        val specifiedSalesEndDate: OffsetDateTime?,
        val tags: List<String>,
        val content: String,
        val draftId: Long?,
    )

    data class Response(
        val id: Long,
    ) : ObjectPayload
}
