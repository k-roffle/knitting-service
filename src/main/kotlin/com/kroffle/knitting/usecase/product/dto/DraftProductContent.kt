package com.kroffle.knitting.usecase.product.dto

data class DraftProductContent(
    val id: Long,
    val knitterId: Long,
    val content: String,
)
