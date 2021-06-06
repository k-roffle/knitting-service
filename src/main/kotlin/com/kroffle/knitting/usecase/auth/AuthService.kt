package com.kroffle.knitting.usecase.auth

import java.net.URI
import java.util.UUID

class AuthService(private val oAuthHelper: GoogleOAuthHelper, private val tokenPublisher: TokenPublisher) {
    fun getAuthorizationUri(): URI = oAuthHelper.getAuthorizationUri()

    fun authorize(): String {
        // TODO Implement
        return tokenPublisher.publish(UUID.randomUUID())
    }

    interface GoogleOAuthHelper {
        fun getAuthorizationUri(): URI
    }

    interface TokenPublisher {
        fun publish(id: UUID): String
    }
}
