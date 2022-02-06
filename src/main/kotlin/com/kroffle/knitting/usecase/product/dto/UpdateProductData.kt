package com.kroffle.knitting.usecase.product.dto

import com.kroffle.knitting.domain.product.value.ProductTag
import com.kroffle.knitting.domain.value.Money
import java.time.OffsetDateTime

data class UpdateProductData(
    val id: Long,
    val knitterId: Long,
    val discountPrice: Money,
    val specifiedSalesStartedAt: OffsetDateTime?,
    val specifiedSalesEndedAt: OffsetDateTime?,
    val content: String,
    val tags: List<ProductTag>,
    val draftId: Long?,
)
