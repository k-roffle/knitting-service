package com.kroffle.knitting.controller.handler.design.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.kroffle.knitting.domain.design.enum.DesignType
import com.kroffle.knitting.domain.design.enum.LevelType
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
    val pattern: String,
    val description: String,
    @JsonProperty("target_level")
    val targetLevel: LevelType,
    @JsonProperty("cover_image_url")
    val coverImageUrl: String,
    val techniques: List<String>,
)
