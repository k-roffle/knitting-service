package com.kroffle.knitting.domain.product.value

import java.time.LocalDateTime

data class ProductTag(
    val name: String,
    val createdAt: LocalDateTime?,
)
