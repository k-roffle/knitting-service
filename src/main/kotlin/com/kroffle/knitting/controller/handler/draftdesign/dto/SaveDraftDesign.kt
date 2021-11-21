package com.kroffle.knitting.controller.handler.draftdesign.dto

import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload

object SaveDraftDesign {
    data class Request(
        val id: Long?,
        val designId: Long?,
        val value: String,
    )

    data class Response(
        val id: Long,
    ) : ObjectPayload
}
