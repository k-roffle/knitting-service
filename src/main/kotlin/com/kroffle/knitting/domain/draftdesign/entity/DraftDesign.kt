package com.kroffle.knitting.domain.draftdesign.entity

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
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
        get() = OBJECT_MAPPER.readValue<Value>(this.value).name

    companion object {
        private val OBJECT_MAPPER = ObjectMapper()

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

    data class Value(
        val name: String? = null,
        @JsonAnySetter
        @get:JsonAnyGetter
        val truncated: MutableMap<String, Any> = sortedMapOf(),
    )
}
