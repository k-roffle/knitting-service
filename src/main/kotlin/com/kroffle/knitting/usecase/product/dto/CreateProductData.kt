package com.kroffle.knitting.usecase.product.dto

import com.kroffle.knitting.domain.product.value.ProductItem
import com.kroffle.knitting.domain.product.value.ProductTag
import com.kroffle.knitting.domain.value.Money
import java.time.OffsetDateTime

data class CreateProductData(
    val knitterId: Long,
    val name: String,
    val fullPrice: Money,
    val discountPrice: Money,
    val representativeImageUrl: String,
    val specifiedSalesStartedAt: OffsetDateTime?,
    val specifiedSalesEndedAt: OffsetDateTime?,
    val content: String,
    val tags: List<ProductTag>,
    val items: List<ProductItem>,
    val draftId: Long?,
)
