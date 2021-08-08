package com.kroffle.knitting.controller.handler.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class BadRequest(message: String) : ResponseStatusException(HttpStatus.BAD_REQUEST, message)
