package com.kroffle.knitting.usecase.draftdesign.dto

data class SaveDraftDesignData(
    val id: Long?,
    val knitterId: Long,
    val designId: Long?,
    val value: String,
)
