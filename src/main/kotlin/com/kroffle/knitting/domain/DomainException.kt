package com.kroffle.knitting.domain

import com.kroffle.knitting.pure.exception.BaseException
import com.kroffle.knitting.pure.exception.ExceptionLayer
import com.kroffle.knitting.pure.exception.HttpStatus

open class DomainException(override val message: String) : BaseException(message = message) {
    override val layer: ExceptionLayer = ExceptionLayer.DOMAIN
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
}
