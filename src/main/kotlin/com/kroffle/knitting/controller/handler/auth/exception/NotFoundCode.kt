package com.kroffle.knitting.controller.handler.auth.exception

import com.kroffle.knitting.controller.ControllerException

class NotFoundCode : ControllerException(message = "code is required")
