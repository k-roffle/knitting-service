package com.kroffle.knitting.infra.persistence.exception

import com.kroffle.knitting.infra.InfraException
import com.kroffle.knitting.pure.exception.HttpStatus

class NotFoundEntity(clazz: Class<*>) : InfraException(message = "Cannot found ${clazz.name}") {
    override val httpStatus: HttpStatus = HttpStatus.NOT_FOUND
}
