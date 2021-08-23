package com.kroffle.knitting.helper.extension

import com.kroffle.knitting.domain.design.value.Pattern

fun Pattern.like(other: Pattern): Boolean {
    if (this === other) return true
    return value == other.value
}
