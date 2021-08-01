package com.kroffle.knitting.controller.handler.auth.dto

data class MyProfileResponse(
    val email: String,
    val profileImageUrl: String?,
    val name: String?,
)
