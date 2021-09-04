package com.kroffle.knitting.controller.handler.product.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload
import com.kroffle.knitting.domain.product.enum.InputStatus
import com.kroffle.knitting.domain.product.value.ProductItem
import com.kroffle.knitting.domain.product.value.ProductTag
import java.time.LocalDate
import java.time.LocalDateTime

data class GetMyProductResponse(
    val id: Long,
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
    val tags: List<ProductTag>,
    val content: String?,
    @JsonProperty("input_status")
    val inputStatus: InputStatus,
    val items: List<ProductItem>,
    @JsonProperty("created_at")
    val createdAt: LocalDateTime?,
    @JsonProperty("updated_at")
    val updatedAt: LocalDateTime?,
) : ObjectPayload
