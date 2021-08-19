package com.kroffle.knitting.controller.handler.helper.response.type

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

class MetaData(
    @JsonProperty("last_cursor")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val lastCursor: String? = null,
)
