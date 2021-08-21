package com.kroffle.knitting.controller.handler.helper.response.type

import com.fasterxml.jackson.annotation.JsonIgnore

interface ListItemPayload : Payload {
    @JsonIgnore
    fun getCursor(): String
}
