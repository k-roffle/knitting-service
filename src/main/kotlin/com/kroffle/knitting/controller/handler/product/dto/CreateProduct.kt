package com.kroffle.knitting.controller.handler.product.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload
import java.time.OffsetDateTime

object CreateProduct {
    data class Request(
        @JsonProperty("design_ids")
        val designIds: List<Long>,
        val name: String,
        @JsonProperty("full_price")
        val fullPrice: Int,
        @JsonProperty("discount_price")
        val discountPrice: Int,
        @JsonProperty("representative_image_url")
        val representativeImageUrl: String,
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
