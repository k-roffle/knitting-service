package com.kroffle.knitting.controller.handler.design.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.kroffle.knitting.domain.design.enum.SizeUnitType

data class NewDesignSize(
    @JsonProperty("size_unit")
    val sizeUnit: SizeUnitType = SizeUnitType.Cm,
    @JsonProperty("total_length")
    val totalLength: Double,
    @JsonProperty("sleeve_length")
    val sleeveLength: Double,
    @JsonProperty("shoulder_width")
    val shoulderWidth: Double,
    @JsonProperty("bottom_width")
    val bottomWidth: Double,
    @JsonProperty("armhole_depth")
    val armholeDepth: Double,
)
