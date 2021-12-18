package com.kroffle.knitting.controller.handler.draftdesign.dto

import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload

object DeleteDraftDesign {
    data class Response(
        val id: Long,
    ) : ObjectPayload
}
