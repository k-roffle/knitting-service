package com.kroffle.knitting.controller.handler.draftproduct.dto

import com.kroffle.knitting.controller.handler.helper.response.type.ListItemPayload
import java.time.OffsetDateTime

object GetMyDraftProducts {
    data class Response(
        val id: Long,
        val name: String?,
        val updatedAt: OffsetDateTime,
    ) : ListItemPayload {
        override fun getCursor(): String = this.id.toString()
    }
}
