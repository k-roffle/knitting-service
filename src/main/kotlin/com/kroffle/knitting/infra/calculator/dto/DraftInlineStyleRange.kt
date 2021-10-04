package com.kroffle.knitting.infra.calculator.dto

import com.fasterxml.jackson.annotation.JsonIgnore

data class DraftInlineStyleRange(
    val offset: Int,
    val length: Int,
    val style: String,
) {
    @JsonIgnore
    fun getCalculateType(): CalculateType? = CalculateType.findByKey(style)

    @JsonIgnore
    fun getStartIndex(): Int = offset

    @JsonIgnore
    fun getEndIndex(): Int = offset + length - 1
}
