package com.kroffle.knitting.infra.oauth.exception

import com.kroffle.knitting.common.exception.HttpStatus
import com.kroffle.knitting.infra.InfraException

class UnavailableGoogle : InfraException(message = "google server is not responding") {
    override val httpStatus: HttpStatus = HttpStatus.SERVICE_UNAVAILABLE
}
