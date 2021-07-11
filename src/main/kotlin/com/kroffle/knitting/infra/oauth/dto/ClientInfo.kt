package com.kroffle.knitting.infra.oauth.dto

data class ClientInfo(
    val scheme: String,
    val host: String,
    val redirectPath: String = "/login/redirected",
)
