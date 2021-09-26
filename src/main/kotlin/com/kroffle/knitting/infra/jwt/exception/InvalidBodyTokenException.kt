package com.kroffle.knitting.infra.jwt.exception

import com.kroffle.knitting.common.exception.HttpStatus
import com.kroffle.knitting.infra.InfraException

class InvalidBodyTokenException : InfraException(message = "Token had invalid body format.") {
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
}
