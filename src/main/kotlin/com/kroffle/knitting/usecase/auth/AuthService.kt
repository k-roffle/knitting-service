package com.kroffle.knitting.usecase.auth

import com.kroffle.knitting.domain.knitter.entity.Knitter
import com.kroffle.knitting.usecase.auth.dto.Profile
import reactor.core.publisher.Mono
import java.net.URI
import java.util.UUID

class AuthService(
    private val oAuthHelper: OAuthHelper,
    private val tokenPublisher: TokenPublisher,
    private val knitterRepository: KnitterRepository,
) {
    fun getAuthorizationUri(): URI = oAuthHelper.getAuthorizationUri()

    fun authorize(code: String): Mono<String> {
        // FIXME #45 이미 존재하는 유저인 경우 프로필 정보를 업데이트해야 합니다.
        // 첫 로그인하는 유저인 경우 유저 정보를 생성해야 합니다.
        return oAuthHelper.getProfile(code).flatMap {
            profile ->
            knitterRepository.findByEmail(profile.email).flatMap {
                knitter ->
                Mono.just(tokenPublisher.publish(knitter.id!!))
            }
        }
    }

    fun refreshToken(userId: UUID): String {
        return tokenPublisher.publish(userId)
    }

    interface OAuthHelper {
        fun getAuthorizationUri(): URI
        fun getProfile(code: String): Mono<Profile>
    }

    interface TokenPublisher {
        fun publish(id: UUID): String
    }

    interface KnitterRepository {
        fun create(user: Knitter): Mono<Knitter>
        fun findByEmail(email: String): Mono<Knitter>
    }
}
