package com.kroffle.knitting.pure.exception

enum class HttpStatus(val code: Int) {
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    FORBIDDEN(403),
    INTERNAL_SERVER_ERROR(500),
}
