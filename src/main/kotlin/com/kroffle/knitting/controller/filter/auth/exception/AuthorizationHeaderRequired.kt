package com.kroffle.knitting.controller.filter.auth.exception

import com.kroffle.knitting.controller.ControllerException

class AuthorizationHeaderRequired : ControllerException(message = "Authorization header is required")
