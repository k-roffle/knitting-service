package com.kroffle.knitting.infra.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SelfProperties {
    @Value("\${self.host}")
    lateinit var host: String

    @Value("\${self.env}")
    lateinit var env: String
}
