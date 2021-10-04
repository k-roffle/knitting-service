package com.kroffle.knitting.controller.handler.exception

import com.kroffle.knitting.controller.ControllerException

class EmptyBodyException : ControllerException(message = "Body is required")
