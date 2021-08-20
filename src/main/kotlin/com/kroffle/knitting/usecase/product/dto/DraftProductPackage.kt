package com.kroffle.knitting.usecase.product.dto

import com.kroffle.knitting.domain.value.Money
import java.time.LocalDate

data class DraftProductPackage(
    val knitterId: Long,
    val name: String,
    val fullPrice: Money,
    val discountPrice: Money,
    val representativeImageUrl: String,
    val specifiedSalesStartDate: LocalDate?,
    val specifiedSalesEndDate: LocalDate?,
    val tags: List<String>,
    val goodsIds: List<Long>,
    val designIds: List<Long>,
)
