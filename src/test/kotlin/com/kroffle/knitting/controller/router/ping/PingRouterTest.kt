package com.kroffle.knitting.controller.router.ping

import com.kroffle.knitting.controller.filter.auth.AuthorizationFilter
import com.kroffle.knitting.controller.handler.ping.PingHandler
import com.kroffle.knitting.helper.WebTestClientHelper
import com.kroffle.knitting.infra.properties.WebApplicationProperties
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@WebFluxTest
@ExtendWith(SpringExtension::class)
class PingRouterTest {
    private lateinit var webClient: WebTestClient

    @MockBean
    private lateinit var tokenDecoder: AuthorizationFilter.TokenDecoder

    @MockBean
    private lateinit var webProperties: WebApplicationProperties

    @BeforeEach
    fun setUp() {
        webClient = WebTestClientHelper
            .createWebTestClient(PingRouter(PingHandler()).pingRouterFunction())
    }

    @Test
    fun `pong 이 잘 반환되어야 함`() {
        webClient
            .get()
            .uri("/ping")
            .exchange()
            .expectStatus().isOk
            .expectBody<String>().isEqualTo("pong")
    }
}
