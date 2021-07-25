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

class TokenDecoder(private val jwtSecretKey: String) : AuthorizationFilter.TokenDecoder {
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
    override fun getAuthorizedUserId(token: String): Long {
        val claims = this.decodeToken(token)
        val id = claims["id"] ?: throw InvalidBodyTokenException()
        return id.asString().toLong()
    }
}
