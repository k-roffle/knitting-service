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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {
    @Autowired
    lateinit var webProperties: WebApplicationProperties

    @Autowired
    lateinit var selfProperties: SelfProperties

    @Autowired
    lateinit var authProperties: AuthProperties

    @Autowired
    lateinit var clientProperties: ClientProperties

    @Bean
    fun tokenDecoder() = TokenDecoder(authProperties.jwtSecretKey)

    @Bean
    fun tokenPublisher() = TokenPublisher(authProperties.jwtSecretKey)

    @Bean
    fun authService(repository: AuthService.KnitterRepository): AuthService {
        val scheme = when (selfProperties.env) {
            "local" -> "http"
            else -> "https"
        }
        val host = clientProperties.host
        val googleClientId = webProperties.googleClientId
        val googleClientSecret = webProperties.googleClientSecret

        return AuthService(
            GoogleOAuthHelperImpl(
                ClientInfo(scheme, host),
                GoogleOAuthConfig(googleClientId, googleClientSecret),
            ),
            tokenPublisher(),
            repository,
        )
    }
}
