package com.kroffle.knitting.controller.handler.draftdesign.dto

import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload
import java.time.OffsetDateTime

object MyDraftDesign {
    data class Response(
        val id: Long? = null,
        val value: String,
        val updatedAt: OffsetDateTime?,
    ) : ObjectPayload
}
