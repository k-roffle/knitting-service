package com.kroffle.knitting.usecase

import com.kroffle.knitting.common.exception.BaseException
import com.kroffle.knitting.common.exception.ExceptionLayer

abstract class UseCaseException(
    override val message: String,
) : BaseException(message) {
    override val layer: ExceptionLayer = ExceptionLayer.USE_CASE
}
