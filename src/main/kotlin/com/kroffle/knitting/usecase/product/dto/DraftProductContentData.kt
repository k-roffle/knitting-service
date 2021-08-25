package com.kroffle.knitting.usecase.product.dto

data class DraftProductContentData(
    val id: Long,
    val knitterId: Long,
    val content: String,
)
