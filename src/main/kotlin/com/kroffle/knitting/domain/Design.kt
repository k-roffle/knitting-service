package com.kroffle.knitting.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table("design")
class Design(
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
)
