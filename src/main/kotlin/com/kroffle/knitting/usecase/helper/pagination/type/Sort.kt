package com.kroffle.knitting.usecase.helper.pagination.type

data class Sort(
    val column: String,
    val direction: SortDirection,
)
