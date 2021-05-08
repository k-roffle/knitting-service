package com.kroffle.knitting.infra.design.calculator.data

import com.fasterxml.jackson.annotation.JsonIgnore

data class DraftInlineStyleRange(
    val offset: Int,
    val length: Int,
    val style: String,
) {
    @JsonIgnore
    fun getCalculateType(): CalculateType? = CalculateType.findByKey(style)
}
