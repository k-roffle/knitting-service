package com.kroffle.knitting.usecase.design.dto

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.design.value.Gauge
import com.kroffle.knitting.domain.design.value.Pattern
import com.kroffle.knitting.domain.design.value.Size
import com.kroffle.knitting.domain.design.value.Technique
import com.kroffle.knitting.domain.value.Money

data class CreateDesignData(
    val knitterId: Long,
    val name: String,
    val designType: Design.DesignType,
    val patternType: Design.PatternType,
    val gauge: Gauge,
    val size: Size,
    val needle: String,
    val yarn: String,
    val extra: String?,
    val price: Money,
    val pattern: Pattern,
    val description: String,
    val targetLevel: Design.LevelType,
    val coverImageUrl: String,
    val techniques: List<Technique>,
    val draftId: Long?,
)
