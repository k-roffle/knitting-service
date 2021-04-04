package com.kroffle.knitting.infra.design.entity

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.design.enum.DesignType
import com.kroffle.knitting.domain.design.enum.PatternType
import com.kroffle.knitting.domain.design.value.Length
import com.kroffle.knitting.domain.design.value.Money
import com.kroffle.knitting.domain.design.value.Size
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table("design")
class DesignEntity(
    @Id val id: UUID = UUID.randomUUID(),
    val name: String,
    val designType: DesignType,
    val patternType: PatternType,
    val stitches: Double,
    val rows: Double,
    val sizeId: UUID,
    val needle: String,
    val yarn: String?,
    val extra: String?,
    val price: Int = 0,
    val pattern: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    // FIXME size 주입받기
    fun toDesign(): Design =
        Design(
            id = this.id,
            name = this.name,
            designType = this.designType,
            patternType = this.patternType,
            stitches = this.stitches,
            rows = this.rows,
            size = Size(Length(1.0), Length(2.0), Length(3.0), Length(4.0), Length(5.0)),
            needle = this.needle,
            yarn = this.yarn,
            extra = this.extra,
            price = Money(this.price),
            pattern = this.pattern,
            createdAt = this.createdAt,
        )
}
