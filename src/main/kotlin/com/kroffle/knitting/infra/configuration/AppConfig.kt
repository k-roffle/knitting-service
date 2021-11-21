package com.kroffle.knitting.infra.configuration

import com.kroffle.knitting.infra.jwt.TokenDecoder
import com.kroffle.knitting.infra.jwt.TokenPublisher
import com.kroffle.knitting.infra.oauth.GoogleOAuthHelperImpl
import com.kroffle.knitting.infra.oauth.dto.ClientInfo
import com.kroffle.knitting.infra.oauth.dto.GoogleOAuthConfig
import com.kroffle.knitting.infra.properties.AuthProperties
import com.kroffle.knitting.infra.properties.ClientProperties
import com.kroffle.knitting.infra.properties.SelfProperties
import com.kroffle.knitting.infra.properties.WebApplicationProperties
import com.kroffle.knitting.usecase.auth.AuthService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig(
    private val webProperties: WebApplicationProperties,
    private val selfProperties: SelfProperties,
    private val authProperties: AuthProperties,
    private val clientProperties: ClientProperties,
) {
    @Bean
    fun tokenDecoder() = TokenDecoder(authProperties.jwtSecretKey)

    @Bean
    fun tokenPublisher() = TokenPublisher(authProperties.jwtSecretKey)

    @Bean
    fun googleOAuthHelper(): AuthService.OAuthHelper {
        val scheme = when (selfProperties.env) {
            "local" -> "http"
            else -> "https"
        }
        val host = clientProperties.host
        val googleClientId = webProperties.googleClientId
        val googleClientSecret = webProperties.googleClientSecret

        return GoogleOAuthHelperImpl(
            ClientInfo(scheme, host),
            GoogleOAuthConfig(googleClientId, googleClientSecret),
        )
    }
}
