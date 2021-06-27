package com.kroffle.knitting.infra.knitter.entity

import com.kroffle.knitting.domain.knitter.entity.Knitter
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table("knitter")
class KnitterEntity(
    @Id private var id: UUID?,
    private val email: String,
    private val name: String?,
    private val profileImageUrl: String?,
    private val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    fun toKnitter(): Knitter =
        Knitter(
            id = this.id,
            email = this.email,
            name = this.name,
            profileImageUrl = this.profileImageUrl,
            createdAt = this.createdAt,
        )
}

fun Knitter.toKnitterEntity() =
    KnitterEntity(
        id = this.id,
        email = this.email,
        name = this.name,
        profileImageUrl = this.profileImageUrl,
        createdAt = this.createdAt ?: LocalDateTime.now(),
    )
