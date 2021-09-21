package com.kroffle.knitting.domain.product.value

import java.time.OffsetDateTime

data class ProductTag(
    val name: String,
    val createdAt: OffsetDateTime?,
)
