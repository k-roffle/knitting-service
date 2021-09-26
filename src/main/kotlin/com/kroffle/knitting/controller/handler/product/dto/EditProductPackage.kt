package com.kroffle.knitting.controller.handler.product.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload
import java.time.LocalDate

object EditProductPackage {
    data class Request(
        val id: Long?,
        val name: String,
        @JsonProperty("full_price")
        val fullPrice: Int,
        @JsonProperty("discount_price")
        val discountPrice: Int,
        @JsonProperty("representative_image_url")
        val representativeImageUrl: String,
        @JsonProperty("specified_sales_start_date")
        val specifiedSalesStartDate: LocalDate?,
        @JsonProperty("specified_sales_end_date")
        val specifiedSalesEndDate: LocalDate?,
        val tags: List<String>,
        @JsonProperty("design_ids")
        val designIds: List<Long>,
    )

    data class Response(
        val id: Long,
    ) : ObjectPayload
}
