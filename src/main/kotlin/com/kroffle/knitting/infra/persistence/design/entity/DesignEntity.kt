package com.kroffle.knitting.infra.persistence.design.entity

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.design.value.Gauge
import com.kroffle.knitting.domain.design.value.Pattern
import com.kroffle.knitting.domain.design.value.Size
import com.kroffle.knitting.domain.design.value.Technique
import com.kroffle.knitting.domain.value.Money
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime

@Table("design")
class DesignEntity(
    @Id private var id: Long?,
    private val knitterId: Long,
    private val name: String,
    private val designType: Design.DesignType,
    private val patternType: Design.PatternType,
    private val stitches: Double,
    private val rows: Double,
    private val needle: String,
    private val yarn: String,
    private val extra: String?,
    private val price: Int,
    private val pattern: String,
    private val description: String,
    private val targetLevel: String,
    private val coverImageUrl: String,
    private val updatedAt: OffsetDateTime = OffsetDateTime.now(),
    private val createdAt: OffsetDateTime = OffsetDateTime.now(),
) {
    fun getNotNullId(): Long = id!!

    fun toDesign(techniques: List<Technique>, size: Size): Design =
        Design(
            id = this.id,
            knitterId = this.knitterId,
            name = this.name,
            designType = this.designType,
            patternType = this.patternType,
            gauge = Gauge(this.stitches, this.rows),
            size = size,
            needle = this.needle,
            yarn = this.yarn,
            extra = this.extra,
            price = Money(this.price),
            pattern = Pattern(this.pattern),
            description = this.description,
            targetLevel = Design.LevelType.valueOf(this.targetLevel),
            coverImageUrl = this.coverImageUrl,
            techniques = techniques,
            updatedAt = this.updatedAt,
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
        needle = this.needle,
        yarn = this.yarn,
        extra = this.extra,
        price = this.price.value,
        pattern = this.pattern.value,
        description = this.description,
        targetLevel = this.targetLevel.name,
        coverImageUrl = this.coverImageUrl,
        updatedAt = this.updatedAt ?: OffsetDateTime.now(),
        createdAt = this.createdAt ?: OffsetDateTime.now(),
    )
