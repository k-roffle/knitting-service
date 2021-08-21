package com.kroffle.knitting.controller.handler.auth.dto

import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload

class RefreshTokenResponse(val token: String) : ObjectPayload
