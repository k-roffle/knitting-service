package com.kroffle.knitting.helper.extension

import com.kroffle.knitting.domain.value.Money

fun Money.like(other: Money): Boolean {
    if (this === other) return true
    return value == other.value
}
