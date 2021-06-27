package com.kroffle.knitting.infra.oauth

import com.kroffle.knitting.infra.properties.SelfProperties
import com.kroffle.knitting.usecase.auth.AuthService
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

class GoogleOauthHelperImpl(
    private val selfProperties: SelfProperties,
    private val googleClientId: String,
    private val googleSecretKey: String,
) : AuthService.GoogleOAuthHelper {

    private fun getCallbackUri(): String {
        val scheme = when (selfProperties.env) {
            "local" -> "http"
            else -> "https"
        }
        return UriComponentsBuilder.newInstance()
            .scheme(scheme)
            .host(selfProperties.host)
            .path("/auth/google/authorized")
            .build()
            .toUriString()
    }

    override fun getAuthorizationUri(): URI =
        URI.create(
            UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("accounts.google.com")
                .path("/o/oauth2/v2/auth")
                .queryParam("scope", "profile+https://www.googleapis.com/auth/userinfo.email")
                .queryParam("access_type", "offline")
                .queryParam("include_granted_scopes", "true")
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", getCallbackUri())
                .queryParam("client_id", googleClientId)
                .build()
                .toUriString()
        )
}
