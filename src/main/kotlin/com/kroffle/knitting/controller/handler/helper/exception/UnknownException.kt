package com.kroffle.knitting.controller.handler.helper.exception

import com.kroffle.knitting.controller.ControllerException
import com.kroffle.knitting.pure.exception.HttpStatus

class UnknownException : ControllerException(
    message = "Unknown exception raised",
    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
)
