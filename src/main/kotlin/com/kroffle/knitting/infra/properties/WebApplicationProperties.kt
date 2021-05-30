package com.kroffle.knitting.infra.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class WebApplicationProperties {
    @Value("\${web.origins}")
    lateinit var origins: List<String>

    @Value("\${web.google.client_id}")
    lateinit var googleClientId: String

    @Value("\${web.jwt_secret_key}")
    lateinit var jwtSecretKey: String
}
