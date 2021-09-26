package com.kroffle.knitting.domain

import com.kroffle.knitting.common.exception.BaseException
import com.kroffle.knitting.common.exception.ExceptionLayer
import com.kroffle.knitting.common.exception.HttpStatus

open class DomainException(override val message: String) : BaseException(message = message) {
    override val layer: ExceptionLayer = ExceptionLayer.DOMAIN
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
}
