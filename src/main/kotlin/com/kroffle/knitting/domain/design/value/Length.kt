package com.kroffle.knitting.domain.design.value

class Length(val value: Double, val unit: Unit = Unit.Cm) {
    init {
        require(value > 0)
    }

    enum class Unit(val code: Int) {
        Cm(1),
    }
}
