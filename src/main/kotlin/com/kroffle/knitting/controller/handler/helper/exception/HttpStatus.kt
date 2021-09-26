package com.kroffle.knitting.controller.handler.helper.exception

import com.kroffle.knitting.pure.exception.HttpStatus
import org.springframework.http.HttpStatus as SpringHttpStatus

fun HttpStatus.to(): SpringHttpStatus {
    return when (this) {
        HttpStatus.BAD_REQUEST -> SpringHttpStatus.BAD_REQUEST
        HttpStatus.UNAUTHORIZED -> SpringHttpStatus.UNAUTHORIZED
        HttpStatus.FORBIDDEN -> SpringHttpStatus.FORBIDDEN
        HttpStatus.NOT_FOUND -> SpringHttpStatus.NOT_FOUND
        HttpStatus.INTERNAL_SERVER_ERROR -> SpringHttpStatus.INTERNAL_SERVER_ERROR
    }
}
