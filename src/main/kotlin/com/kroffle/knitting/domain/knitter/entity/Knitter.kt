package com.kroffle.knitting.domain.knitter.entity

import java.time.LocalDateTime
import java.util.UUID

class Knitter(
    val id: UUID? = null,
    val email: String,
    val name: String?,
    val imageUrl: String?,
    val createdAt: LocalDateTime?,
)
