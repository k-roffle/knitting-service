package com.kroffle.knitting.infra.oauth.exception

import com.kroffle.knitting.common.exception.HttpStatus
import com.kroffle.knitting.infra.InfraException

class InvalidGoogleAccessToken : InfraException(message = "google access token is invalid") {
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
}
