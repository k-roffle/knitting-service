package com.kroffle.knitting.controller.handler.design.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload
import com.kroffle.knitting.domain.design.entity.Design

object NewDesign {
    data class Request(
        val name: String,
        @JsonProperty("design_type")
        val designType: Design.DesignType,
        @JsonProperty("pattern_type")
        val patternType: Design.PatternType,
        val stitches: Double,
        val rows: Double,
        val size: SizeDto,
        val needle: String,
        val yarn: String,
        val extra: String?,
        val price: Int,
        val pattern: String,
        val description: String,
        @JsonProperty("target_level")
        val targetLevel: Design.LevelType,
        @JsonProperty("cover_image_url")
        val coverImageUrl: String,
        val techniques: List<String>,
        @JsonProperty("draft_id")
        val draftId: Long?,
    )

    data class Response(
        val id: Long,
    ) : ObjectPayload
}
