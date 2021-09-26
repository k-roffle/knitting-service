package com.kroffle.knitting.common.exception

abstract class BaseException(override val message: String) : Exception(message) {
    abstract val httpStatus: HttpStatus
    abstract val layer: ExceptionLayer
}
