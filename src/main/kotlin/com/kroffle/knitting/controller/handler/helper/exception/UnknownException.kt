package com.kroffle.knitting.controller.handler.helper.exception

import com.kroffle.knitting.common.exception.HttpStatus
import com.kroffle.knitting.controller.ControllerException

class UnknownException : ControllerException(
    message = "Unknown exception raised",
    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
)
