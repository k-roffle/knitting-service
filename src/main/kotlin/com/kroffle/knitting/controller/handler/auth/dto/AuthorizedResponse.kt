package com.kroffle.knitting.controller.handler.auth.dto

import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload

class AuthorizedResponse(val token: String) : ObjectPayload
