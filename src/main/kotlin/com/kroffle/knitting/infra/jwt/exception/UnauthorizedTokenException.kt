package com.kroffle.knitting.infra.jwt.exception

import com.kroffle.knitting.controller.filter.auth.exception.TokenDecodeException

class UnauthorizedTokenException : TokenDecodeException("Token is unauthorized.")
