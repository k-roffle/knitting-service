package com.kroffle.knitting.controller.handler.design.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.kroffle.knitting.controller.handler.helper.response.type.ListItemPayload

class MyDesign(
    val id: Long,
    val name: String,
    val yarn: String,
    @JsonProperty("cover_image_url")
    val coverImageUrl: String,
    val tags: List<String>,
) : ListItemPayload {
    override fun getCursor(): String = id.toString()
}
