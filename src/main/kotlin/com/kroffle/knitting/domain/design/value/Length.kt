package com.kroffle.knitting.domain.design.value

import com.kroffle.knitting.domain.design.enum.SizeUnitType

class Length(val value: Double, val unit: SizeUnitType = SizeUnitType.Cm) {
    init {
        require(value > 0)
    }
}
