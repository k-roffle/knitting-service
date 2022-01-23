package com.kroffle.knitting.helper.extension

import com.kroffle.knitting.controller.handler.design.dto.MyDesigns

fun MyDesigns.Response.like(other: MyDesigns.Response): Boolean {
    if (this === other) return true
    return id == other.id &&
        name == other.name &&
        yarn == other.yarn &&
        coverImageUrl == other.coverImageUrl &&
        tags == other.tags &&
        createdAt.isEqual(other.createdAt)
}
