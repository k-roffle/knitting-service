package com.kroffle.knitting.controller.handler.helper.response.type

import com.fasterxml.jackson.annotation.JsonIgnore

interface ListItemData : ObjectData {
    @JsonIgnore
    fun getCursor(): String
}
