package com.kroffle.knitting.usecase.draftproduct.dto

data class SaveDraftProductData(
    val id: Long?,
    val knitterId: Long,
    val productId: Long?,
    val value: String,
)
