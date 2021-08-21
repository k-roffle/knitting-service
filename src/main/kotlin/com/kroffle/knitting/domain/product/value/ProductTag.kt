package com.kroffle.knitting.domain.product.value

import java.time.LocalDateTime

/* FIXME: #99 이슈 진행하며 id 제거가 가능하다면 제거, 불가능하다면 entity 로 변경합니다. */
data class ProductTag(
    val id: Long?,
    val tag: String,
    val createdAt: LocalDateTime?,
)
