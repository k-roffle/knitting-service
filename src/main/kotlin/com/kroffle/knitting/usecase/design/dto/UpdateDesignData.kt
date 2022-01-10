package com.kroffle.knitting.usecase.design.dto

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.design.value.Gauge
import com.kroffle.knitting.domain.design.value.Pattern
import com.kroffle.knitting.domain.design.value.Size
import com.kroffle.knitting.domain.design.value.Technique

data class UpdateDesignData(
    val id: Long,
    val knitterId: Long,
    val designType: Design.DesignType,
    val patternType: Design.PatternType,
    val gauge: Gauge,
    val size: Size,
    val needle: String,
    val yarn: String,
    val extra: String?,
    val pattern: Pattern,
    val description: String,
    val targetLevel: Design.LevelType,
    val techniques: List<Technique>,
    val draftId: Long?,
)
