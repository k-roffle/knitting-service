package com.kroffle.knitting.data.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class DatabaseProperties {
    @Value("\${db.host}")
    lateinit var host: String

    @Value("\${db.username}")
    lateinit var username: String

    @Value("\${db.password}")
    lateinit var password: String

    @Value("\${db.database}")
    lateinit var database: String
}
