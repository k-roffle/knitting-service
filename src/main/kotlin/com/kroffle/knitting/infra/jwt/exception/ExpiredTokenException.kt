package com.kroffle.knitting.infra.jwt.exception

import com.kroffle.knitting.infra.InfraException
import com.kroffle.knitting.pure.exception.HttpStatus

class ExpiredTokenException : InfraException(message = "Token is expired.") {
    override val httpStatus: HttpStatus = HttpStatus.UNAUTHORIZED
}
