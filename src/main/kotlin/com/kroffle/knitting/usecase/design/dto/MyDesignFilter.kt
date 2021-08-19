package com.kroffle.knitting.usecase.design.dto

import com.kroffle.knitting.usecase.helper.pagination.type.Paging
import com.kroffle.knitting.usecase.helper.pagination.type.Sort

data class MyDesignFilter(
    val knitterId: Long,
    val paging: Paging,
    val sort: Sort,
)
