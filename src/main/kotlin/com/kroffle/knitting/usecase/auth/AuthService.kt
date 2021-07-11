package com.kroffle.knitting.usecase.auth

import com.kroffle.knitting.domain.knitter.entity.Knitter
import com.kroffle.knitting.usecase.auth.dto.Profile
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.net.URI
import java.util.UUID

class AuthService(
    private val oAuthHelper: OAuthHelper,
    private val tokenPublisher: TokenPublisher,
    private val knitterRepository: KnitterRepository,
) {
    fun getAuthorizationUri(): URI = oAuthHelper.getAuthorizationUri()

    fun authorize(code: String): Mono<String> {
        return oAuthHelper.getProfile(code).flatMap {
            profile ->
            knitterRepository
                .findByEmail(profile.email)
                .flatMap {
                    knitter ->
                    knitterRepository.update(
                        Knitter(
                            id = knitter.id,
                            email = knitter.email,
                            name = profile.name,
                            profileImageUrl = profile.profileImageUrl,
                            createdAt = knitter.createdAt,
                        )
                    )
                }
                .switchIfEmpty {
                    knitterRepository.create(
                        Knitter(
                            id = null,
                            email = profile.email,
                            name = profile.name,
                            profileImageUrl = profile.profileImageUrl,
                            createdAt = null,
                        )
                    )
                }
                .flatMap {
                    Mono.just(tokenPublisher.publish(it.id!!))
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
        fun update(user: Knitter): Mono<Knitter>
        fun findByEmail(email: String): Mono<Knitter>
    }
}
