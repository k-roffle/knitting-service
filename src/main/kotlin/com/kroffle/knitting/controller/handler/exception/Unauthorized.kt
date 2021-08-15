package com.kroffle.knitting.controller.handler.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class Unauthorized(message: String) : ResponseStatusException(HttpStatus.UNAUTHORIZED, message)
