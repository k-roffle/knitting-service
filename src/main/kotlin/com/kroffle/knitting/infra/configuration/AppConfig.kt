package com.kroffle.knitting.infra.configuration

import com.kroffle.knitting.infra.jwt.TokenDecoder
import com.kroffle.knitting.infra.jwt.TokenPublisher
import com.kroffle.knitting.infra.oauth.GoogleOAuthHelperImpl
import com.kroffle.knitting.infra.oauth.dto.ClientInfo
import com.kroffle.knitting.infra.oauth.dto.GoogleOAuthConfig
import com.kroffle.knitting.infra.persistence.design.DBDesignRepository
import com.kroffle.knitting.infra.persistence.design.R2dbcDesignRepository
import com.kroffle.knitting.infra.persistence.knitter.DBKnitterRepository
import com.kroffle.knitting.infra.persistence.knitter.R2dbcKnitterRepository
import com.kroffle.knitting.infra.properties.AuthProperties
import com.kroffle.knitting.infra.properties.ClientProperties
import com.kroffle.knitting.infra.properties.SelfProperties
import com.kroffle.knitting.infra.properties.WebApplicationProperties
import com.kroffle.knitting.usecase.auth.AuthService
import com.kroffle.knitting.usecase.design.DesignService
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
    fun designRepository(dbDesignRepository: DBDesignRepository) = R2dbcDesignRepository(dbDesignRepository)

    @Bean
    fun designService(repository: DesignService.DesignRepository) = DesignService(repository)

    @Bean
    fun userRepository(dbKnitterRepository: DBKnitterRepository) = R2dbcKnitterRepository(dbKnitterRepository)

    @Bean
    fun authService(repository: AuthService.KnitterRepository) = AuthService(
        GoogleOAuthHelperImpl(
            ClientInfo(
                when (selfProperties.env) {
                    "local" -> "http"
                    else -> "https"
                },
                clientProperties.host,
            ),
            GoogleOAuthConfig(
                webProperties.googleClientId,
                webProperties.googleClientSecret,
            ),
        ),
        tokenPublisher(),
        repository,
    )
}
