package com.kroffle.knitting.infra.oauth.dto

data class GoogleProfileResponse(
    val email: String,
    val name: String,
    val picture: String,
)
