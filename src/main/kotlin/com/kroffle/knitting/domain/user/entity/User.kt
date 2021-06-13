package com.kroffle.knitting.domain.user.entity

import java.time.LocalDateTime
import java.util.UUID

class User(
    private val id: UUID? = null,
    private val email: String,
    private val name: String?,
    private val imageUrl: String?,
    private val createdAt: LocalDateTime?,
)
