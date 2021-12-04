package com.kroffle.knitting.domain.value

data class Money(val value: Int) {
    operator fun compareTo(money: Money): Int =
        this.value.compareTo(money.value)

    companion object {
        val ZERO = Money(0)
    }
}
