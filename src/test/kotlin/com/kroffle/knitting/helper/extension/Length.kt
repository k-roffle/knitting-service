package com.kroffle.knitting.helper.extension

import com.kroffle.knitting.domain.design.value.Length

fun Length.like(other: Length): Boolean {
    if (this === other) return true
    return value == other.value &&
        unit == other.unit
}
