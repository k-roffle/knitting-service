package com.kroffle.knitting.controller.handler.design

import com.kroffle.knitting.controller.handler.design.dto.NewDesignRequest
import com.kroffle.knitting.domain.design.entity.Design
import org.springframework.validation.Errors
import org.springframework.validation.Validator

class DesignValidator : Validator {
    override fun supports(clazz: Class<*>): Boolean =
        Design::class.java == clazz

    override fun validate(target: Any, errors: Errors) {
        val designInput = target as NewDesignRequest
        if (designInput.price < 0) {
            errors.rejectValue("price", "negative_price")
        }
    }
}
