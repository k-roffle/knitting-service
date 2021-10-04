package com.kroffle.knitting.infra.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.Claim
import com.kroffle.knitting.controller.filter.auth.AuthorizationFilter
import com.kroffle.knitting.infra.jwt.exception.ExpiredTokenException
import com.kroffle.knitting.infra.jwt.exception.InvalidBodyTokenException
import com.kroffle.knitting.infra.jwt.exception.UnauthorizedTokenException

class TokenDecoder(private val jwtSecretKey: String) : AuthorizationFilter.TokenDecoder {
    override fun getKnitterId(token: String): Long {
        val claims = this.decodeToken(token)
        val id = claims["id"] ?: throw InvalidBodyTokenException()
        return id.asString().toLong()
    }

    private fun decodeToken(token: String): Map<String, Claim> {
        try {
            val jwt = JWT.require(Algorithm.HMAC256(jwtSecretKey))
                .build()
                .verify(token)
            return jwt.claims
        } catch (error: TokenExpiredException) {
            throw ExpiredTokenException()
        } catch (error: JWTVerificationException) {
            throw UnauthorizedTokenException()
        }
    }
}
