package com.kroffle.knitting.infra.oauth

import com.kroffle.knitting.infra.properties.SelfProperties
import com.kroffle.knitting.infra.properties.WebApplicationProperties
import com.kroffle.knitting.usecase.auth.GoogleLogInHandler
import org.springframework.web.util.UriComponentsBuilder

class GoogleOauthHelperImpl(
    private val selfProperties: SelfProperties,
    private val webProperties: WebApplicationProperties
) : GoogleLogInHandler.GoogleOAuthHelper {

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

    override fun getAuthorizationUri(): String =
        UriComponentsBuilder.newInstance()
            .scheme("https")
            .host("accounts.google.com")
            .path("/o/oauth2/v2/auth")
            .queryParam("scope", "profile")
            .queryParam("access_type", "offline")
            .queryParam("include_granted_scopes", "true")
            .queryParam("response_type", "code")
            .queryParam("redirect_uri", getCallbackUri())
            .queryParam("client_id", webProperties.googleClientId)
            .build()
            .toUriString()
}
