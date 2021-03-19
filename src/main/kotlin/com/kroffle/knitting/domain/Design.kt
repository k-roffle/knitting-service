package com.kroffle.knitting.domain

import org.springframework.data.annotation.Id
import java.time.LocalDateTime
import java.util.UUID

data class Design(
        // TODO size, pattern 추가
        @Id val id: UUID = UUID.randomUUID(),
        val name: String,
        val designType: DesignType,
        val patternType: PatternType,
        val stitches: Float,
        val rows: Float,
        val needle: String,
        val yarn: String?,
        val extra: String?,
        val price: Int = 0,
        val createdAt: LocalDateTime = LocalDateTime.now()
)
