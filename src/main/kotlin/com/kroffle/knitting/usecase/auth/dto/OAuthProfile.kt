package com.kroffle.knitting.usecase.auth.dto

data class OAuthProfile(
    val email: String,
    val name: String,
    val profileImageUrl: String?,
)
