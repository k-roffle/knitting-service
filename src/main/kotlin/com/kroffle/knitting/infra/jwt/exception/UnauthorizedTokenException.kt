package com.kroffle.knitting.infra.jwt.exception

import com.kroffle.knitting.common.exception.HttpStatus
import com.kroffle.knitting.infra.InfraException

class UnauthorizedTokenException : InfraException(message = "Token is unauthorized.") {
    override val httpStatus: HttpStatus = HttpStatus.UNAUTHORIZED
}
