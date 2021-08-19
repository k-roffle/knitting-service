package com.kroffle.knitting.domain.design.entity

import com.kroffle.knitting.domain.design.enum.DesignType
import com.kroffle.knitting.domain.design.enum.LevelType
import com.kroffle.knitting.domain.design.enum.PatternType
import com.kroffle.knitting.domain.design.value.Gauge
import com.kroffle.knitting.domain.design.value.Pattern
import com.kroffle.knitting.domain.design.value.Size
import java.time.LocalDateTime

class Design(
    val id: Long? = null,
    val knitterId: Long,
    val name: String,
    val designType: DesignType,
    val patternType: PatternType,
    val gauge: Gauge,
    val size: Size,
    val needle: String,
    val yarn: String,
    val extra: String?,
    val pattern: Pattern,
    val description: String,
    val targetLevel: LevelType,
    val coverImageUrl: String,
    val createdAt: LocalDateTime?,
)
