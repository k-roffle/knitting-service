package com.kroffle.knitting.usecase.auth

import java.net.URI
import java.util.UUID

class AuthService(private val oAuthHelper: GoogleOAuthHelper, private val tokenHelper: TokenHelper) {
    fun getAuthorizationUri(): URI = oAuthHelper.getAuthorizationUri()

    fun authorize(): String {
        // TODO Implement
        return tokenHelper.publish(UUID.randomUUID())
    }

    interface GoogleOAuthHelper {
        fun getAuthorizationUri(): URI
    }

    interface TokenHelper {
        fun publish(id: UUID): String
    }
}
