package com.kroffle.knitting.usecase

import com.kroffle.knitting.pure.exception.BaseException
import com.kroffle.knitting.pure.exception.ExceptionLayer

abstract class UseCaseException(
    override val message: String,
) : BaseException(message) {
    override val layer: ExceptionLayer = ExceptionLayer.USE_CASE
}
