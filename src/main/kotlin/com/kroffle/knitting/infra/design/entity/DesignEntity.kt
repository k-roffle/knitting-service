package com.kroffle.knitting.infra.design.entity

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.design.enum.DesignType
import com.kroffle.knitting.domain.design.enum.PatternType
import com.kroffle.knitting.domain.design.value.Length
import com.kroffle.knitting.domain.design.value.Money
import com.kroffle.knitting.domain.design.value.Size
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table("design")
class DesignEntity(
    @Id private val id: UUID = UUID.randomUUID(),
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
    private val yarn: String?,
    private val extra: String?,
    private val price: Int = 0,
    private val pattern: String,
    private val createdAt: LocalDateTime = LocalDateTime.now(),
    @Transient private val isNew: Boolean = true,
) : Persistable<UUID> {
    override fun isNew(): Boolean {
        return isNew
    }

    override fun getId(): UUID {
        return this.id
    }

    fun toDesign(): Design =
        Design(
            id = this.id,
            name = this.name,
            designType = this.designType,
            patternType = this.patternType,
            stitches = this.stitches,
            rows = this.rows,
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
            price = Money(this.price),
            pattern = this.pattern,
            createdAt = this.createdAt,
        )
}

fun Design.toDesignEntity() =
    DesignEntity(
        id = this.id,
        name = this.name,
        designType = this.designType,
        patternType = this.patternType,
        stitches = this.stitches,
        rows = this.rows,
        totalLength = this.size.totalLength.value,
        sleeveLength = this.size.sleeveLength.value,
        shoulderWidth = this.size.shoulderWidth.value,
        bottomWidth = this.size.bottomWidth.value,
        armholeDepth = this.size.armholeDepth.value,
        needle = this.needle,
        yarn = this.yarn,
        extra = this.extra,
        price = this.price.value,
        pattern = this.pattern,
        createdAt = this.createdAt,
    )