package com.kroffle.knitting.usecase.auth.dto

data class Profile(
    val email: String,
    val name: String,
    val profileImageUrl: String?,
)
