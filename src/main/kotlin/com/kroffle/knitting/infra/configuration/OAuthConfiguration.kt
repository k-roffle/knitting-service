package com.kroffle.knitting.infra.configuration

import com.kroffle.knitting.infra.oauth.GoogleOauthHelperImpl
import com.kroffle.knitting.infra.properties.SelfProperties
import com.kroffle.knitting.infra.properties.WebApplicationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OAuthConfiguration {
    @Autowired
    lateinit var webProperties: WebApplicationProperties

    @Autowired
    lateinit var selfProperties: SelfProperties

    @Bean
    fun googleOAuthHelper() = GoogleOauthHelperImpl(selfProperties, webProperties)
}
