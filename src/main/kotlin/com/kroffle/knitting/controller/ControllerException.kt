package com.kroffle.knitting.controller

import com.kroffle.knitting.pure.exception.BaseException
import com.kroffle.knitting.pure.exception.ExceptionLayer
import com.kroffle.knitting.pure.exception.HttpStatus

open class ControllerException(
    override val message: String,
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
) : BaseException(message = message) {
    override val layer: ExceptionLayer = ExceptionLayer.CONTROLLER
}
