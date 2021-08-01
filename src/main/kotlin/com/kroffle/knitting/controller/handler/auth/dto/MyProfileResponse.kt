package com.kroffle.knitting.controller.handler.auth.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class MyProfileResponse(
    val email: String,
    @JsonProperty("profile_image_url")
    val profileImageUrl: String?,
    val name: String?,
)
