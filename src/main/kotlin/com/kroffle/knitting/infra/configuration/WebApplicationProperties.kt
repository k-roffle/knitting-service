package com.kroffle.knitting.infra.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class WebApplicationProperties {
    @Value("\${web.origins}")
    lateinit var origins: List<String>
}
