package com.kroffle.knitting.controller.handler.product.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload
import java.time.OffsetDateTime

// TODO: 수정 가능한 범위 정하기 with 해선
object UpdateProduct {
    data class Request(
        @JsonProperty("discount_price")
        val discountPrice: Int,
        @JsonProperty("specified_sales_start_date")
        val specifiedSalesStartDate: OffsetDateTime?,
        @JsonProperty("specified_sales_end_date")
        val specifiedSalesEndDate: OffsetDateTime?,
        val tags: List<String>,
        val content: String,
        @JsonProperty("draft_id")
        val draftId: Long?,
    )

    data class Response(
        val id: Long,
    ) : ObjectPayload
}
