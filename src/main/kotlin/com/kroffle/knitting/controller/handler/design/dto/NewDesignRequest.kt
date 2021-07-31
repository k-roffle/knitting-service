package com.kroffle.knitting.controller.handler.design.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.kroffle.knitting.domain.design.enum.DesignType
import com.kroffle.knitting.domain.design.enum.PatternType

data class NewDesignRequest(
    val name: String,
    @JsonProperty("design_type")
    val designType: DesignType,
    @JsonProperty("pattern_type")
    val patternType: PatternType,
    val stitches: Double,
    val rows: Double,
    val size: NewDesignSize,
    val needle: String,
    val yarn: String,
    val extra: String?,
    val price: Int,
    val pattern: String,
)
