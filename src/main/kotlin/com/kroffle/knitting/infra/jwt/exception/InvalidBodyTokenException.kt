package com.kroffle.knitting.infra.jwt.exception

import com.kroffle.knitting.controller.filter.auth.exception.TokenDecodeException

class InvalidBodyTokenException : TokenDecodeException("Token had invalid body format.")
