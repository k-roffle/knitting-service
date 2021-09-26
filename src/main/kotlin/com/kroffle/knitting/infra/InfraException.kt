package com.kroffle.knitting.infra

import com.kroffle.knitting.common.exception.BaseException
import com.kroffle.knitting.common.exception.ExceptionLayer

abstract class InfraException(override val message: String) : BaseException(message = message) {
    override val layer: ExceptionLayer = ExceptionLayer.INFRA
}
