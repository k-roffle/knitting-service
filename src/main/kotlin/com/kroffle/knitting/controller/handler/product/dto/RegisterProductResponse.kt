package com.kroffle.knitting.controller.handler.product.dto

import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload

data class RegisterProductResponse(
    val id: Long,
) : ObjectPayload
