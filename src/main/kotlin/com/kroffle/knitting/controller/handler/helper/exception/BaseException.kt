package com.kroffle.knitting.controller.handler.helper.exception

import com.kroffle.knitting.common.exception.BaseException
import org.springframework.web.server.ResponseStatusException

fun BaseException.to(): ResponseStatusException =
    ResponseStatusException(this.httpStatus.to(), this.message)
