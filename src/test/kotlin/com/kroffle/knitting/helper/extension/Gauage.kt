package com.kroffle.knitting.helper.extension

import com.kroffle.knitting.domain.design.value.Gauge

fun Gauge.like(other: Gauge): Boolean {
    if (this === other) return true
    return stitches == other.stitches &&
        rows == other.rows
}
