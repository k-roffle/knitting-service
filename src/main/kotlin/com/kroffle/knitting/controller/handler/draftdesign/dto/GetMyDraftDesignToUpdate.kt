package com.kroffle.knitting.controller.handler.draftdesign.dto

import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload
import java.time.OffsetDateTime

object GetMyDraftDesignToUpdate {
    data class Response(
        val id: Long,
        val value: String,
        val updatedAt: OffsetDateTime,
    ) : ObjectPayload
}
