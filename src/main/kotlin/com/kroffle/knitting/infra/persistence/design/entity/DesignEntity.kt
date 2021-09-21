package com.kroffle.knitting.infra.persistence.design.entity

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.design.enum.DesignType
import com.kroffle.knitting.domain.design.enum.LevelType
import com.kroffle.knitting.domain.design.enum.PatternType
import com.kroffle.knitting.domain.design.value.Gauge
import com.kroffle.knitting.domain.design.value.Length
import com.kroffle.knitting.domain.design.value.Pattern
import com.kroffle.knitting.domain.design.value.Size
import com.kroffle.knitting.domain.design.value.Technique
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("design")
class DesignEntity(
    @Id private var id: Long?,
    private val knitterId: Long,
    private val name: String,
    private val designType: DesignType,
    private val patternType: PatternType,
    private val stitches: Double,
    private val rows: Double,
    private val totalLength: Double,
    private val sleeveLength: Double,
    private val shoulderWidth: Double,
    private val bottomWidth: Double,
    private val armholeDepth: Double,
    private val needle: String,
    private val yarn: String,
    private val extra: String?,
    private val pattern: String,
    private val description: String,
    private val targetLevel: String,
    private val coverImageUrl: String,
    private val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    fun getNotNullId(): Long = id!!

    fun toDesign(techniques: List<Technique>): Design =
        Design(
            id = this.id,
            knitterId = this.knitterId,
            name = this.name,
            designType = this.designType,
            patternType = this.patternType,
            gauge = Gauge(this.stitches, this.rows),
            size = Size(
                totalLength = Length(this.totalLength),
                sleeveLength = Length(this.sleeveLength),
                shoulderWidth = Length(this.shoulderWidth),
                bottomWidth = Length(this.bottomWidth),
                armholeDepth = Length(this.armholeDepth),
            ),
            needle = this.needle,
            yarn = this.yarn,
            extra = this.extra,
            pattern = Pattern(this.pattern),
            description = this.description,
            targetLevel = LevelType.getFromKey(this.targetLevel),
            coverImageUrl = this.coverImageUrl,
            techniques = techniques,
            createdAt = this.createdAt,
        )
}

fun Design.toDesignEntity() =
    DesignEntity(
        id = this.id,
        knitterId = this.knitterId,
        name = this.name,
        designType = this.designType,
        patternType = this.patternType,
        stitches = this.gauge.stitches,
        rows = this.gauge.rows,
        totalLength = this.size.totalLength.value,
        sleeveLength = this.size.sleeveLength.value,
        shoulderWidth = this.size.shoulderWidth.value,
        bottomWidth = this.size.bottomWidth.value,
        armholeDepth = this.size.armholeDepth.value,
        needle = this.needle,
        yarn = this.yarn,
        extra = this.extra,
        pattern = this.pattern.value,
        description = this.description,
        targetLevel = this.targetLevel.key,
        coverImageUrl = this.coverImageUrl,
        createdAt = this.createdAt ?: LocalDateTime.now(),
    )
