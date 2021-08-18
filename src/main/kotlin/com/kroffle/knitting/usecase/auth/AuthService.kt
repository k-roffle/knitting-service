package com.kroffle.knitting.usecase.auth

import com.kroffle.knitting.domain.knitter.entity.Knitter
import com.kroffle.knitting.usecase.auth.dto.OAuthProfile
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.net.URI

class AuthService(
    private val oAuthHelper: OAuthHelper,
    private val tokenPublisher: TokenPublisher,
    private val knitterRepository: KnitterRepository,
) {
    fun getAuthorizationUri(): URI = oAuthHelper.getAuthorizationUri()

    fun authorize(code: String): Mono<String> {
        return oAuthHelper
            .getProfile(code)
            .flatMap {
                profile ->
                knitterRepository
                    .findByEmail(profile.email)
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

    fun refreshToken(knitterId: Long): String {
        return tokenPublisher.publish(knitterId)
    }

    fun getKnitter(knitterId: Long): Mono<Knitter> =
        knitterRepository.findById(knitterId)

    interface OAuthHelper {
        fun getAuthorizationUri(): URI
        fun getProfile(code: String): Mono<OAuthProfile>
    }

    interface TokenPublisher {
        fun publish(id: Long): String
    }

    interface KnitterRepository {
        fun create(user: Knitter): Mono<Knitter>
        fun findByEmail(email: String): Mono<Knitter>
        fun findById(id: Long): Mono<Knitter>
    }
}
