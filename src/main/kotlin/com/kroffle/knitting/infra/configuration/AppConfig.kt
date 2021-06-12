package com.kroffle.knitting.infra.configuration

import com.kroffle.knitting.infra.jwt.TokenHelper
import com.kroffle.knitting.infra.oauth.GoogleOauthHelperImpl
import com.kroffle.knitting.infra.properties.AuthProperties
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

    @Bean
    fun designService(repository: DesignService.DesignRepository) = DesignService(repository)

    @Bean
    fun authService() = AuthService(
        GoogleOauthHelperImpl(selfProperties, webProperties.googleClientId),
        TokenHelper(authProperties.jwtSecretKey)
    )
}