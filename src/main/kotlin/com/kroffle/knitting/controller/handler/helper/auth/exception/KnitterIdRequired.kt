package com.kroffle.knitting.controller.handler.helper.auth.exception

import com.kroffle.knitting.common.exception.HttpStatus
import com.kroffle.knitting.controller.ControllerException

class KnitterIdRequired : ControllerException(message = "knitterId is required", HttpStatus.FORBIDDEN)
