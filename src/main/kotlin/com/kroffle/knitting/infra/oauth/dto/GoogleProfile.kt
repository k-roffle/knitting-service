package com.kroffle.knitting.infra.oauth.dto

object GoogleProfile {
    data class Response(
        val email: String,
        val name: String,
        val picture: String,
    )
}
