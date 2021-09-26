package com.kroffle.knitting.infra.jwt.exception

import com.kroffle.knitting.infra.InfraException
import com.kroffle.knitting.pure.exception.HttpStatus

class InvalidBodyTokenException : InfraException(message = "Token had invalid body format.") {
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
}
