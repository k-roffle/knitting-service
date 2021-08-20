package com.kroffle.knitting.domain.value

class Money(val value: Int) {
    operator fun compareTo(money: Money): Int =
        this.value.compareTo(money.value)

    companion object {
        val MIN = Money(0)
    }
}
