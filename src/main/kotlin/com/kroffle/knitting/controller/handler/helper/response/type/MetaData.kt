package com.kroffle.knitting.controller.handler.helper.response.type

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class MetaData(
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val lastCursor: String? = null,
)
