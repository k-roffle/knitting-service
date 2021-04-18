package com.kroffle.knitting.domain.design.entity

import com.kroffle.knitting.domain.design.enum.DesignType
import com.kroffle.knitting.domain.design.enum.PatternType
import com.kroffle.knitting.domain.design.value.Gauge
import com.kroffle.knitting.domain.design.value.Money
import com.kroffle.knitting.domain.design.value.Pattern
import com.kroffle.knitting.domain.design.value.Size
import java.time.LocalDateTime
import java.util.UUID

class Design(
    val id: UUID,
    val name: String,
    val designType: DesignType,
    val patternType: PatternType,
    val gauge: Gauge,
    val size: Size,
    val needle: String,
    val yarn: String?,
    val extra: String?,
    val price: Money,
    val pattern: Pattern,
    val createdAt: LocalDateTime,
)
