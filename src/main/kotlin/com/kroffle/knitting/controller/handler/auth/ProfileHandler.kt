package com.kroffle.knitting.controller.handler.auth

import com.kroffle.knitting.controller.handler.auth.dto.MyProfileResponse
import com.kroffle.knitting.controller.handler.helper.auth.AuthHelper
import com.kroffle.knitting.controller.handler.helper.response.ResponseHelper
import com.kroffle.knitting.usecase.auth.AuthService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class ProfileHandler(private val authService: AuthService) {
    fun getMyProfile(req: ServerRequest): Mono<ServerResponse> {
        val knitterId = AuthHelper.getAuthenticatedId(req)
        return authService
            .getKnitter(knitterId)
            .map {
                knitter ->
                MyProfileResponse(
                    email = knitter.email,
                    name = knitter.name,
                    profileImageUrl = knitter.profileImageUrl,
                )
            }
            .flatMap {
                ResponseHelper.makeJsonResponse(it)
            }
    }
}
