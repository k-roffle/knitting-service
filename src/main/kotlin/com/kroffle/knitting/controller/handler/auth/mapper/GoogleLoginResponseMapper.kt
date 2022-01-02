package com.kroffle.knitting.controller.handler.auth.mapper

import com.kroffle.knitting.controller.handler.auth.dto.Authorized
import com.kroffle.knitting.controller.handler.auth.dto.RefreshToken

object GoogleLoginResponseMapper {
    fun toAuthorizedResponse(token: String): Authorized.Response =
        Authorized.Response(token)

    fun toRefreshTokenResponse(token: String): RefreshToken.Response =
        RefreshToken.Response(token)
}
