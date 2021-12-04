package com.kroffle.knitting.controller.handler.exception

import com.kroffle.knitting.controller.ControllerException

class InvalidBodyException : ControllerException(message = "Body is invalid format")
