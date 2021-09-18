package com.kroffle.knitting.domain.knitter.entity

import java.time.OffsetDateTime

class Knitter(
    val id: Long? = null,
    val email: String,
    val name: String?,
    val profileImageUrl: String?,
    val createdAt: OffsetDateTime?,
)
