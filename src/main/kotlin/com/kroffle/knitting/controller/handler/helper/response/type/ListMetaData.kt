package com.kroffle.knitting.controller.handler.helper.response.type

import com.fasterxml.jackson.annotation.JsonProperty

data class ListMetaData(
    @JsonProperty("last_cursor")
    val lastCursor: String? = null,
) : MetaData()
