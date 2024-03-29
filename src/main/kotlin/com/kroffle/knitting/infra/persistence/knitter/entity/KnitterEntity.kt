package com.kroffle.knitting.infra.persistence.knitter.entity

import com.kroffle.knitting.domain.knitter.entity.Knitter
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime

@Table("knitter")
class KnitterEntity(
    @Id private var id: Long?,
    private val email: String,
    private val name: String?,
    private val profileImageUrl: String?,
    private val createdAt: OffsetDateTime = OffsetDateTime.now(),
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
        createdAt = this.createdAt ?: OffsetDateTime.now(),
    )
