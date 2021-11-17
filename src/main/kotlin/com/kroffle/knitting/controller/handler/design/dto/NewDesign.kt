package com.kroffle.knitting.controller.handler.design.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload
import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.design.value.Length

object NewDesign {
    data class Request(
        val name: String,
        @JsonProperty("design_type")
        val designType: Design.DesignType,
        @JsonProperty("pattern_type")
        val patternType: Design.PatternType,
        val stitches: Double,
        val rows: Double,
        val size: NewDesignSize,
        val needle: String,
        val yarn: String,
        val extra: String?,
        val pattern: String,
        val description: String,
        @JsonProperty("target_level")
        val targetLevel: Design.LevelType,
        @JsonProperty("cover_image_url")
        val coverImageUrl: String,
        val techniques: List<String>,
    ) {
        data class NewDesignSize(
            @JsonProperty("size_unit")
            val sizeUnit: Length.Unit = Length.Unit.Cm,
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
    }

    data class Response(
        val id: Long,
    ) : ObjectPayload
}
