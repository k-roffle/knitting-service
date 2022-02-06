package com.kroffle.knitting.controller.handler.design.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.kroffle.knitting.controller.handler.helper.response.type.ListItemPayload
import java.time.OffsetDateTime

object MyDesigns {
    data class Response(
        val id: Long,
        val name: String,
        val yarn: String,
        @JsonProperty("cover_image_url")
        val coverImageUrl: String,
        val tags: List<String>,
        @JsonProperty("created_at")
        val createdAt: OffsetDateTime,
    ) : ListItemPayload {
        override fun getCursor(): String = id.toString()
    }
}
