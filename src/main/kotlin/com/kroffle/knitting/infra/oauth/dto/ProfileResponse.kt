package com.kroffle.knitting.infra.oauth.dto

data class ProfileResponse(
    val email: String,
    val name: String,
    val picture: String,
)
