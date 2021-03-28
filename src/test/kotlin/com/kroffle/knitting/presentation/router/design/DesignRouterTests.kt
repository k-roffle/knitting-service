package com.kroffle.knitting.presentation.router.design

import com.kroffle.knitting.data.entity.design.DesignEntity
import com.kroffle.knitting.data.entity.design.DesignType
import com.kroffle.knitting.data.entity.design.PatternType
import com.kroffle.knitting.data.repository.design.DesignRepository
import com.kroffle.knitting.domain.handler.design.DesignHandler
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import java.time.LocalDateTime
import java.util.UUID

@WebFluxTest
@ExtendWith(SpringExtension::class)
class DesignRouterTests {
    @MockBean
    lateinit var repo: DesignRepository

    private lateinit var webClient: WebTestClient

    private lateinit var design: DesignEntity

    private lateinit var now: LocalDateTime

    @BeforeEach
    fun setUp() {
        now = LocalDateTime.now()
        val sizeId = UUID.fromString("00000000-0000-0000-0000-000000000000")
        design = DesignEntity(
            id = UUID.fromString("00000000-0000-0000-0000-000000000000"),
            name = "test",
            designType = DesignType.Sweater,
            patternType = PatternType.Text,
            stitches = 23.5,
            rows = 25.0,
            sizeId = sizeId,
            needle = "5.0mm",
            yarn = null,
            extra = null,
            price = 0,
            pattern = "# Step1. 코를 10개 잡습니다.",
            createdAt = now,
        )

        val routerFunction = DesignRouter(DesignHandler(repo)).designRouterFunction()
        webClient = WebTestClient.bindToRouterFunction(routerFunction).build()
    }

    @Test
    @Throws(Exception::class)
    fun `design 리스트가 잘 반환되어야 함`() {
        given(repo.findAll()).willReturn(Flux.just(design))
        val mockId = UUID.fromString("00000000-0000-0000-0000-000000000000")
        val responseBody: List<DesignEntity>? = webClient
            .get()
            .uri("/designs/")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk
            .expectBodyList(DesignEntity::class.java)
            .hasSize(1)
            .returnResult()
            .responseBody

        val firstResponseBody = responseBody?.get(0)
        assertThat(firstResponseBody?.id).isEqualTo(mockId)
        assertThat(firstResponseBody?.name).isEqualTo("test")
        assertThat(firstResponseBody?.designType).isEqualTo(DesignType.Sweater)
        assertThat(firstResponseBody?.patternType).isEqualTo(PatternType.Text)
        assertThat(firstResponseBody?.stitches).isEqualTo(23.5)
        assertThat(firstResponseBody?.rows).isEqualTo(25.0)
        assertThat(firstResponseBody?.sizeId).isEqualTo(mockId)
        assertThat(firstResponseBody?.needle).isEqualTo("5.0mm")
        assertThat(firstResponseBody?.yarn).isEqualTo(null)
        assertThat(firstResponseBody?.extra).isEqualTo(null)
        assertThat(firstResponseBody?.price).isEqualTo(0)
        assertThat(firstResponseBody?.pattern).isEqualTo("# Step1. 코를 10개 잡습니다.")
        assertThat(firstResponseBody?.createdAt).isEqualTo(now)
    }
}
