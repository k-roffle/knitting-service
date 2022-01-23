package com.kroffle.knitting.domain.draftdesign.entity

import com.kroffle.knitting.domain.helper.DraftValueReader
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

    val name: String?
        get() = DraftValueReader.read(value, ParsedValue::name)

    data class ParsedValue(
        val name: String? = null,
    ) : DraftValueReader.TruncatedValue()

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
