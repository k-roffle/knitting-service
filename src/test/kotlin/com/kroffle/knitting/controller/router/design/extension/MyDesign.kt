package com.kroffle.knitting.controller.router.design.extension

import com.kroffle.knitting.controller.handler.design.dto.MyDesign

fun MyDesign.like(other: MyDesign): Boolean {
    if (this === other) return true
    return id == other.id &&
        name == other.name &&
        yarn == other.yarn &&
        coverImageUrl == other.coverImageUrl &&
        tags == other.tags
}
