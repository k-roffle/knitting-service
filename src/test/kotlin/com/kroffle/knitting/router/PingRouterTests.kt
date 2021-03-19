package com.kroffle.knitting.router

import com.kroffle.knitting.handler.PingHandler
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@WebFluxTest
@ExtendWith(SpringExtension::class)
class PingRouterTests {
    lateinit var webClient: WebTestClient

    @BeforeEach
    fun setUp() {
        val routerFunction = PingRouter(PingHandler()).pingRouterFunction()
        webClient = WebTestClient.bindToRouterFunction(routerFunction).build()
    }

    @Test
    fun `pong 이 잘 반환되어야 함`() {
        webClient
            .get()
            .uri("/ping/")
            .exchange()
            .expectStatus().isOk
            .expectBody<String>().isEqualTo("pong")
    }
}
