package com.kroffle.knitting.domain.design.entity

import com.kroffle.knitting.domain.design.enum.DesignType
import com.kroffle.knitting.domain.design.enum.LevelType
import com.kroffle.knitting.domain.design.enum.PatternType
import com.kroffle.knitting.domain.design.value.Gauge
import com.kroffle.knitting.domain.design.value.Pattern
import com.kroffle.knitting.domain.design.value.Size
import com.kroffle.knitting.domain.design.value.Technique
import java.time.OffsetDateTime

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
    val techniques: List<Technique>,
    val createdAt: OffsetDateTime?,
) {
    companion object {
        fun new(
            knitterId: Long,
            name: String,
            designType: DesignType,
            patternType: PatternType,
            gauge: Gauge,
            size: Size,
            needle: String,
            yarn: String,
            extra: String?,
            pattern: Pattern,
            description: String,
            targetLevel: LevelType,
            coverImageUrl: String,
            techniques: List<Technique>,
        ) = Design(
            id = null,
            knitterId = knitterId,
            name = name,
            designType = designType,
            patternType = patternType,
            gauge = gauge,
            size = size,
            needle = needle,
            yarn = yarn,
            extra = extra,
            pattern = pattern,
            description = description,
            targetLevel = targetLevel,
            coverImageUrl = coverImageUrl,
            techniques = techniques,
            createdAt = null,
        )
    }
}
