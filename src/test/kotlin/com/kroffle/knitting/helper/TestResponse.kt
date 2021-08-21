package com.kroffle.knitting.helper

import com.kroffle.knitting.controller.handler.helper.response.type.MetaData

class TestResponse<T>(
    val payload: T,
    val meta: MetaData,
)
