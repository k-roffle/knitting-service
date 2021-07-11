package com.kroffle.knitting.infra.oauth.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class AccessTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String,
)
