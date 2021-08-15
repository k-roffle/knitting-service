package com.kroffle.knitting.controller.handler.auth

import com.kroffle.knitting.controller.handler.auth.dto.MyProfileResponse
import com.kroffle.knitting.controller.handler.exception.Unauthorized
import com.kroffle.knitting.usecase.auth.AuthService
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono

@Component
class ProfileHandler(private val authService: AuthService) {
    fun getMyProfile(req: ServerRequest): Mono<ServerResponse> {
        val userId = req.attribute("userId")
        if (userId.isEmpty) {
            throw Unauthorized("userId is required")
        }
        return authService
            .getKnitter(userId.get() as Long)
            .flatMap {
                knitter ->
                ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(
                        MyProfileResponse(
                            email = knitter.email,
                            name = knitter.name,
                            profileImageUrl = knitter.profileImageUrl,
                        )
                    )
            }
    }
}
