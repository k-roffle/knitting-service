package com.kroffle.knitting.controller

import com.kroffle.knitting.common.exception.BaseException
import com.kroffle.knitting.common.exception.ExceptionLayer
import com.kroffle.knitting.common.exception.HttpStatus

open class ControllerException(
    override val message: String,
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
) : BaseException(message = message) {
    override val layer: ExceptionLayer = ExceptionLayer.CONTROLLER
}
