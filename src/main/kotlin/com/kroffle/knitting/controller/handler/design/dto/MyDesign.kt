package com.kroffle.knitting.controller.handler.design.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.kroffle.knitting.controller.handler.helper.response.type.ListItemData

class MyDesign(
    val id: Long,
    val name: String,
    val yarn: String,
    @JsonProperty("thumbnail_image_url")
    val thumbnailImageUrl: String?,
    val tags: List<String>,
) : ListItemData {
    override fun getCursor(): String = id.toString()
}
