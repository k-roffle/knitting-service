package com.kroffle.knitting.infra.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AuthProperties {
    @Value("\${web.jwt_secret_key}")
    lateinit var jwtSecretKey: String
}
