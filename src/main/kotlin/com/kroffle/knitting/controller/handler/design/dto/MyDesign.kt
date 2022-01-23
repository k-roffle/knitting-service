package com.kroffle.knitting.controller.handler.design.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload
import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.design.value.Gauge
import com.kroffle.knitting.domain.design.value.Pattern
import com.kroffle.knitting.domain.design.value.Technique
import com.kroffle.knitting.domain.value.Money
import java.time.OffsetDateTime

object MyDesign {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class Response(
        val id: Long,
        val name: String,
        val designType: Design.DesignType,
        val patternType: Design.PatternType,
        val gauge: Gauge,
        val size: SizeDto,
        val needle: String,
        val yarn: String,
        val extra: String?,
        val price: Money,
        val pattern: Pattern,
        val description: String,
        val targetLevel: Design.LevelType,
        val coverImageUrl: String,
        val techniques: List<Technique>,
        val updatedAt: OffsetDateTime?,
        val createdAt: OffsetDateTime?,
    ) : ObjectPayload
}
