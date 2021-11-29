package com.kroffle.knitting.domain.design.entity

import com.kroffle.knitting.domain.design.value.Gauge
import com.kroffle.knitting.domain.design.value.Pattern
import com.kroffle.knitting.domain.design.value.Size
import com.kroffle.knitting.domain.design.value.Technique
import com.kroffle.knitting.domain.value.Money
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
    val price: Money,
    val pattern: Pattern,
    val description: String,
    val targetLevel: LevelType,
    val coverImageUrl: String,
    val techniques: List<Technique>,
    val createdAt: OffsetDateTime?,
) {
    enum class DesignType(val code: Int, val tag: String) {
        Sweater(1, "니트"),
    }

    enum class PatternType(val code: Int, val tag: String) {
        Text(1, "서술형도안"),
        Image(2, "이미지도안"),
        Video(3, "영상도안"),
    }

    enum class LevelType {
        PERSON_BY_PERSON,
        EASY,
        NORMAL,
        HARD
    }

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
            price: Money,
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
            price = price,
            pattern = pattern,
            description = description,
            targetLevel = targetLevel,
            coverImageUrl = coverImageUrl,
            techniques = techniques,
            createdAt = null,
        )
    }
}
