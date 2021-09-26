package com.kroffle.knitting.controller.handler.helper.auth.exception

import com.kroffle.knitting.controller.ControllerException
import com.kroffle.knitting.pure.exception.HttpStatus

class KnitterIdRequired : ControllerException(message = "knitterId is required", HttpStatus.FORBIDDEN)
