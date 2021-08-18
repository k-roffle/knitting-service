package com.kroffle.knitting.domain.value

class Money(val value: Int) {
    fun getDiscountPercent(discountPrice: Money): Int {
        return discountPrice.value / value * 100
    }
}
