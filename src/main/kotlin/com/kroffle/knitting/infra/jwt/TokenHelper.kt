package com.kroffle.knitting.infra.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Claim
import com.kroffle.knitting.pure.extensions.toDate
import com.kroffle.knitting.controller.filter.auth.AuthorizationFilter
import com.kroffle.knitting.usecase.auth.AuthService
import java.time.LocalDateTime
import java.time.ZoneId
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

    private fun decodeToken(token: String): Map<String, Claim> {
        val jwt = JWT.require(Algorithm.HMAC256(jwtSecretKey))
            .build()
            .verify(token)
        return jwt.claims
    }

    override fun getAuthorizedUserId(token: String): UUID? {
        val claims = this.decodeToken(token)
        val id = claims["id"] ?: return null
        return UUID.fromString(id.asString())
    }

    override fun validateToken(token: String): Boolean {
        val userId = getAuthorizedUserId(token)
        val exp = getAuthorizationExp(token)
        return userId != null && exp != null
    }

    fun getAuthorizationExp(token: String): LocalDateTime? {
        val claims = this.decodeToken(token)
        val exp = claims["exp"] ?: return null
        return LocalDateTime.ofInstant(exp.asDate().toInstant(), ZoneId.systemDefault())
    }

    companion object {
        const val EXPIRATION_TIME = (60 * 60 * 24).toLong()
    }
}
