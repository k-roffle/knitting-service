package com.kroffle.knitting.infra.oauth

import com.kroffle.knitting.infra.oauth.dto.ClientInfo
import com.kroffle.knitting.infra.oauth.dto.GoogleAccessTokenResponse
import com.kroffle.knitting.infra.oauth.dto.GoogleOAuthConfig
import com.kroffle.knitting.infra.oauth.dto.GoogleProfileResponse
import com.kroffle.knitting.infra.oauth.exception.InvalidGoogleAccessToken
import com.kroffle.knitting.infra.oauth.exception.InvalidGoogleCode
import com.kroffle.knitting.infra.oauth.exception.UnavailableGoogle
import com.kroffle.knitting.usecase.auth.AuthService
import com.kroffle.knitting.usecase.auth.dto.OAuthProfile
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import java.net.URI

class GoogleOAuthHelperImpl(
    private val clientInfo: ClientInfo,
    private val googleOAuthConfig: GoogleOAuthConfig,
) : AuthService.OAuthHelper {

    private fun getCallbackUri(): String {
        return UriComponentsBuilder.newInstance()
            .scheme(clientInfo.scheme)
            .host(clientInfo.host)
            .path(clientInfo.redirectPath)
            .build()
            .toUriString()
    }

    private fun getAccessToken(code: String): Mono<String> {
        val webClient = WebClient.create("https://oauth2.googleapis.com")
        return webClient
            .post()
            .uri("/token")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                mapOf(
                    "code" to code,
                    "client_id" to googleOAuthConfig.clientId,
                    "client_secret" to googleOAuthConfig.secretKey,
                    "redirect_uri" to getCallbackUri(),
                    "grant_type" to "authorization_code",
                )
            )
            .retrieve()
            .onStatus(HttpStatus::isError) {
                when (it.statusCode()) {
                    HttpStatus.BAD_REQUEST -> throw InvalidGoogleCode()
                    else -> throw UnavailableGoogle()
                }
            }
            .bodyToMono<GoogleAccessTokenResponse>()
            .flatMap {
                Mono.just(it.accessToken)
            }
    }

    override fun getAuthorizationUri(): URI =
        URI.create(
            UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("accounts.google.com")
                .path("/o/oauth2/v2/auth")
                .queryParam("scope", SCOPES.joinToString("+"))
                .queryParam("access_type", "offline")
                .queryParam("include_granted_scopes", "true")
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", getCallbackUri())
                .queryParam("client_id", googleOAuthConfig.clientId)
                .build()
                .toUriString()
        )

    override fun getProfile(code: String): Mono<OAuthProfile> {
        val webClient = WebClient.create("https://www.googleapis.com")
        return getAccessToken(code).flatMap {
            webClient
                .get()
                .uri { uriBuilder ->
                    uriBuilder
                        .path("/oauth2/v3/userinfo")
                        .queryParam("access_token", it)
                        .build()
                }
                .retrieve()
                .onStatus(HttpStatus::isError) {
                    when (it.statusCode()) {
                        HttpStatus.BAD_REQUEST -> throw InvalidGoogleAccessToken()
                        else -> throw UnavailableGoogle()
                    }
                }
                .bodyToMono(GoogleProfileResponse::class.java)
                .flatMap {
                    Mono.just(
                        OAuthProfile(
                            email = it.email,
                            name = it.name,
                            profileImageUrl = it.picture,
                        )
                    )
                }
        }
    }
    companion object {
        private val SCOPES = listOf(
            "profile",
            "https://www.googleapis.com/auth/userinfo.email",
        )
    }
}
