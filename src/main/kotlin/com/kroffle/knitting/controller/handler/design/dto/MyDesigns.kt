package com.kroffle.knitting.controller.handler.design.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.kroffle.knitting.controller.handler.helper.response.type.ListItemPayload
import java.time.OffsetDateTime

object MyDesigns {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class Response(
        val id: Long,
        val name: String,
        val yarn: String,
        val coverImageUrl: String,
        val tags: List<String>,
        val createdAt: OffsetDateTime,
    ) : ListItemPayload {
        override fun getCursor(): String = id.toString()
    }
}
