package com.kroffle.knitting.controller.handler.design.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload
import com.kroffle.knitting.domain.design.entity.Design

object NewDesign {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class Request(
        val name: String,
        val designType: Design.DesignType,
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
        val targetLevel: Design.LevelType,
        val coverImageUrl: String,
        val techniques: List<String>,
        val draftId: Long?,
    )

    data class Response(
        val id: Long,
    ) : ObjectPayload
}
