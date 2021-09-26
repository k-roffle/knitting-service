package com.kroffle.knitting.infra.jwt.exception

import com.kroffle.knitting.common.exception.HttpStatus
import com.kroffle.knitting.infra.InfraException

class ExpiredTokenException : InfraException(message = "Token is expired.") {
    override val httpStatus: HttpStatus = HttpStatus.UNAUTHORIZED
}
