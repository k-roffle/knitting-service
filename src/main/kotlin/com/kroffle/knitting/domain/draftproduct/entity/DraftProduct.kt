package com.kroffle.knitting.domain.draftproduct.entity

import com.kroffle.knitting.domain.helper.DraftValueReader
import java.time.OffsetDateTime

data class DraftProduct(
    val id: Long? = null,
    val knitterId: Long,
    val value: String,
    val productId: Long?,
    val createdAt: OffsetDateTime?,
    val updatedAt: OffsetDateTime?,
) {
    fun merge(value: String): DraftProduct =
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
        fun new(knitterId: Long, productId: Long?, value: String): DraftProduct =
            DraftProduct(
                id = null,
                knitterId = knitterId,
                productId = productId,
                value = value,
                createdAt = OffsetDateTime.now(),
                updatedAt = OffsetDateTime.now(),
            )
    }
}
