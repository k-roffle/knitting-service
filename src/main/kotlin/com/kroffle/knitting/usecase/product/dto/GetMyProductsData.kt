package com.kroffle.knitting.usecase.product.dto

import com.kroffle.knitting.usecase.helper.pagination.type.Paging
import com.kroffle.knitting.usecase.helper.pagination.type.Sort

data class GetMyProductsData(
    val knitterId: Long,
    val paging: Paging,
    val sort: Sort,
)
