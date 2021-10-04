package com.kroffle.knitting.controller.handler.knitter.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload

object MyProfile {
    data class Response(
        val email: String,
        @JsonProperty("profile_image_url")
        val profileImageUrl: String?,
        val name: String?,
    ) : ObjectPayload
}
