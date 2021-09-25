package com.kroffle.knitting.pure.exception

abstract class BaseException(override val message: String) : Exception(message) {
    abstract val httpStatus: HttpStatus
    abstract val layer: ExceptionLayer
}
