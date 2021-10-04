package com.kroffle.knitting.infra.persistence.exception

import com.kroffle.knitting.common.exception.HttpStatus
import com.kroffle.knitting.infra.InfraException

class NotFoundEntity(clazz: Class<*>) : InfraException(message = "Cannot found ${clazz.name}") {
    override val httpStatus: HttpStatus = HttpStatus.NOT_FOUND
}
