package com.kroffle.knitting.controller.handler.draftproduct.dto

import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload

object DeleteDraftProduct {
    data class Response(
        val id: Long,
    ) : ObjectPayload
}
