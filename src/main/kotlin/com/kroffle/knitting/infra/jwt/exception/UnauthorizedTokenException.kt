package com.kroffle.knitting.infra.jwt.exception

import com.kroffle.knitting.infra.InfraException
import com.kroffle.knitting.pure.exception.HttpStatus

class UnauthorizedTokenException : InfraException(message = "Token is unauthorized.") {
    override val httpStatus: HttpStatus = HttpStatus.UNAUTHORIZED
}
