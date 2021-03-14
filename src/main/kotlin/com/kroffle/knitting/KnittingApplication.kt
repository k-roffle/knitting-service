package com.kroffle.knitting

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KnittingApplication

fun main(args: Array<String>) {
    runApplication<KnittingApplication>(*args)
}
