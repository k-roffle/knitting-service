package com.kroffle.knitting.infra

import com.kroffle.knitting.pure.exception.BaseException
import com.kroffle.knitting.pure.exception.ExceptionLayer

abstract class InfraException(override val message: String) : BaseException(message = message) {
    override val layer: ExceptionLayer = ExceptionLayer.INFRA
}
