package com.kroffle.knitting.infra.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.kroffle.knitting.pure.extensions.toDate
import com.kroffle.knitting.usecase.auth.AuthService
import java.time.OffsetDateTime

class TokenPublisher(private val jwtSecretKey: String) : AuthService.TokenPublisher {
    override fun publish(id: Long): String {
        val now = OffsetDateTime.now()
        val expiredAt = now.plusSeconds(EXPIRATION_TIME)

        return JWT.create()
            .withClaim("id", id.toString())
            .withIssuedAt(now.toLocalDateTime().toDate())
            .withExpiresAt(expiredAt.toLocalDateTime().toDate())
            .sign(Algorithm.HMAC256(jwtSecretKey))
    }

    companion object {
        private const val EXPIRATION_TIME = (60 * 60 * 24).toLong()
    }
}
