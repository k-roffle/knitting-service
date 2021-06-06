package com.kroffle.knitting.infra.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.Claim
import com.kroffle.knitting.controller.exception.auth.ExpiredTokenException
import com.kroffle.knitting.controller.exception.auth.InvalidBodyTokenException
import com.kroffle.knitting.controller.exception.auth.UnauthorizedTokenException
import com.kroffle.knitting.controller.filter.auth.AuthorizationFilter
import com.kroffle.knitting.pure.extensions.toDate
import com.kroffle.knitting.usecase.auth.AuthService
import java.time.LocalDateTime
import java.util.UUID

class TokenHelper(private val jwtSecretKey: String) : AuthService.TokenHelper, AuthorizationFilter.TokenHelper {
    override fun publish(id: UUID): String {
        val now = LocalDateTime.now()
        val expiredAt = now.plusSeconds(EXPIRATION_TIME)

        return JWT.create()
            .withClaim("id", id.toString())
            .withIssuedAt(now.toDate())
            .withExpiresAt(expiredAt.toDate())
            .sign(Algorithm.HMAC256(jwtSecretKey))
    }

    @Throws(ExpiredTokenException::class, UnauthorizedTokenException::class)
    private fun decodeToken(token: String): Map<String, Claim> {
        try {
            val jwt = JWT.require(Algorithm.HMAC256(jwtSecretKey))
                .build()
                .verify(token)
            return jwt.claims
        } catch (e: TokenExpiredException) {
            throw ExpiredTokenException()
        } catch (e: JWTVerificationException) {
            throw UnauthorizedTokenException()
        }
    }

    @Throws(InvalidBodyTokenException::class)
    override fun getAuthorizedUserId(token: String): UUID {
        val claims = this.decodeToken(token)
        val id = claims["id"] ?: throw InvalidBodyTokenException()
        return UUID.fromString(id.asString())
    }

    companion object {
        const val EXPIRATION_TIME = (60 * 60 * 24).toLong()
    }
}
