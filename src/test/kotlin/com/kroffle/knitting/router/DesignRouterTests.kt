package com.kroffle.knitting.router

import com.kroffle.knitting.domain.Design
import com.kroffle.knitting.domain.DesignRepository
import com.kroffle.knitting.domain.DesignType
import com.kroffle.knitting.domain.PatternType
import com.kroffle.knitting.handler.DesignHandler
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import java.time.LocalDateTime
import java.util.UUID

@WebFluxTest
@RunWith(SpringRunner::class)
class DesignRouterTests {
    @MockBean
    lateinit var repo: DesignRepository

    private lateinit var webClient: WebTestClient

    private lateinit var design: Design

    private lateinit var now: LocalDateTime

    @Before
    fun setUp() {
        now = LocalDateTime.now()
        design = Design(
            id = UUID.fromString("00000000-0000-0000-0000-000000000000"),
            name = "test",
            designType = DesignType.Blanket,
            patternType = PatternType.Image,
            stitches = 23.5f,
            rows = 25.0f,
            needle = "5.0mm",
            yarn = null,
            extra = null,
            price = 0,
            createdAt = now,
        )

        val routerFunction = DesignRouter(DesignHandler(repo)).designRouterFunction()
        webClient = WebTestClient.bindToRouterFunction(routerFunction).build()
    }

    @Test
    @Throws(Exception::class)
    fun `design 리스트가 잘 반환되어야 함`() {
        given(repo.findAll()).willReturn(Flux.just(design))

        val responseBody: List<Design>? = webClient.get().uri("/designs/")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Design::class.java)
            .hasSize(1)
            .returnResult().responseBody

        val firstResponseBody = responseBody?.get(0)
        assertThat(firstResponseBody?.id).isEqualTo(UUID.fromString("00000000-0000-0000-0000-000000000000"))
        assertThat(firstResponseBody?.name).isEqualTo("test")
        assertThat(firstResponseBody?.designType).isEqualTo(DesignType.Blanket)
        assertThat(firstResponseBody?.patternType).isEqualTo(PatternType.Image)
        assertThat(firstResponseBody?.stitches).isEqualTo(23.5f)
        assertThat(firstResponseBody?.needle).isEqualTo("5.0mm")
        assertThat(firstResponseBody?.yarn).isEqualTo(null)
        assertThat(firstResponseBody?.extra).isEqualTo(null)
        assertThat(firstResponseBody?.price).isEqualTo(0)
        assertThat(firstResponseBody?.createdAt).isEqualTo(now)
    }
}
