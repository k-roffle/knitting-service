package com.kroffle.knitting.helper.extension

import com.kroffle.knitting.domain.product.value.Money

fun Money.like(other: Money): Boolean {
    if (this === other) return true
    return value == other.value
}
