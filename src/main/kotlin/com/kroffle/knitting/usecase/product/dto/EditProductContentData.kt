package com.kroffle.knitting.usecase.product.dto

data class EditProductContentData(
    val id: Long,
    val knitterId: Long,
    val content: String,
)
