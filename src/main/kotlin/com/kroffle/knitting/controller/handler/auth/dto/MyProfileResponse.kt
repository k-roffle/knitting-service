package com.kroffle.knitting.controller.handler.auth.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectData

data class MyProfileResponse(
    val email: String,
    @JsonProperty("profile_image_url")
    val profileImageUrl: String?,
    val name: String?,
) : ObjectData
