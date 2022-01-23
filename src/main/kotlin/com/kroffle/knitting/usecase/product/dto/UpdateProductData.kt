package com.kroffle.knitting.usecase.product.dto

import com.kroffle.knitting.domain.product.value.ProductTag
import com.kroffle.knitting.domain.value.Money
import java.time.LocalDate

data class UpdateProductData(
    val id: Long,
    val knitterId: Long,
    val discountPrice: Money,
    val specifiedSalesStartDate: LocalDate?,
    val specifiedSalesEndDate: LocalDate?,
    val content: String,
    val tags: List<ProductTag>,
    val draftId: Long?,
)
