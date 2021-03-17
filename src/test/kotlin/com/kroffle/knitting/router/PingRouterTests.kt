package com.kroffle.knitting.router

import com.kroffle.knitting.handler.PingHandler
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@WebFluxTest
@RunWith(SpringRunner::class)
class PingRouterTests {
    lateinit var webClient: WebTestClient

    @Before
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
