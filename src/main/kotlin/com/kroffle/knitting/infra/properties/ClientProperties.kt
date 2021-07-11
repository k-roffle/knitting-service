package com.kroffle.knitting.infra.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ClientProperties {
    @Value("\${client.host}")
    lateinit var host: String
}
