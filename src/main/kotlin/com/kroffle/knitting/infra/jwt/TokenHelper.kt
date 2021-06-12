package com.kroffle.knitting.infra.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Claim
import com.kroffle.knitting.pure.extensions.toDate
import com.kroffle.knitting.usecase.auth.AuthService
import java.time.LocalDateTime
import java.util.UUID

class TokenHelper(private val jwtSecretKey: String) : AuthService.TokenHelper {
    override fun publish(id: UUID): String {
        val now = LocalDateTime.now()
        val expiredAt = now.plusSeconds(EXPIRATION_TIME)

        return JWT.create()
            .withClaim("id", id.toString())
            .withIssuedAt(now.toDate())
            .withExpiresAt(expiredAt.toDate())
            .sign(Algorithm.HMAC256(jwtSecretKey))
    }

    fun decodeToken(token: String): Map<String, Claim> {
        val jwt = JWT.require(Algorithm.HMAC256(jwtSecretKey))
            .build()
            .verify(token)
        return jwt.claims
    }

    companion object {
        const val EXPIRATION_TIME = (60 * 60 * 24).toLong()
    }
}
