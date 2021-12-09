package com.kroffle.knitting

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.extensions.spring.SpringExtension

@Suppress("unused")
class KotestConfig : AbstractProjectConfig() {
    override fun extensions() = listOf(SpringExtension)
}
