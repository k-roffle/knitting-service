package com.kroffle.knitting.domain.draftdesign.entity

import java.time.OffsetDateTime

data class DraftDesign(
    val id: Long? = null,
    val knitterId: Long,
    val value: String,
    val designId: Long?,
    val createdAt: OffsetDateTime?,
    val updatedAt: OffsetDateTime?,
) {
    fun merge(value: String): DraftDesign =
        this.copy(
            value = value,
            updatedAt = OffsetDateTime.now(),
        )

    companion object {
        fun new(knitterId: Long, designId: Long?, value: String): DraftDesign =
            DraftDesign(
                id = null,
                knitterId = knitterId,
                designId = designId,
                value = value,
                createdAt = OffsetDateTime.now(),
                updatedAt = OffsetDateTime.now(),
            )
    }
}
