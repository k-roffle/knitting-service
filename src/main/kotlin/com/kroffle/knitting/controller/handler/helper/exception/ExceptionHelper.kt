package com.kroffle.knitting.controller.handler.helper.exception

import com.kroffle.knitting.common.exception.BaseException
import org.springframework.web.server.ResponseStatusException

object ExceptionHelper {
    private fun convertException(exception: Throwable): ResponseStatusException =
        if (exception is BaseException) {
            exception.to()
        } else {
            UnknownException().to()
        }

    fun raiseException(exception: Throwable) {
        val statusException = convertException(exception)
        throw statusException
    }
}
