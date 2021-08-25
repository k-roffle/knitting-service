package com.kroffle.knitting.helper.extension

import com.kroffle.knitting.domain.design.value.Size

fun Size.like(other: Size): Boolean {
    if (this === other) return true
    return totalLength.like(other.totalLength) &&
        sleeveLength.like(other.sleeveLength) &&
        shoulderWidth.like(other.shoulderWidth) &&
        bottomWidth.like(other.bottomWidth) &&
        armholeDepth.like(other.armholeDepth)
}
