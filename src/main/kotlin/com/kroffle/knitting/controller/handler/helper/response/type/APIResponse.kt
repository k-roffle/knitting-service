package com.kroffle.knitting.controller.handler.helper.response.type

data class APIResponse<Data>(
    val data: Data,
    val meta: MetaData,
)
