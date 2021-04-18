package com.kroffle.knitting.controller.design

import com.fasterxml.jackson.databind.ObjectMapper
import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.design.enum.DesignType
import com.kroffle.knitting.domain.design.enum.PatternType
import com.kroffle.knitting.infra.design.entity.DesignEntity
import com.kroffle.knitting.usecase.design.DesignHandler
import com.kroffle.knitting.usecase.design.DesignRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@WebFluxTest
@ExtendWith(SpringExtension::class)
class DesignRouterTest {
    @MockBean
    lateinit var repo: DesignRepository

    private lateinit var webClient: WebTestClient

    private lateinit var design: Design

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        design = DesignEntity(
            name = "test",
            designType = DesignType.Sweater,
            patternType = PatternType.Text,
            stitches = 23.5,
            rows = 25.0,
            totalLength = 1.0,
            sleeveLength = 2.0,
            shoulderWidth = 3.0,
            bottomWidth = 4.0,
            armholeDepth = 5.0,
            needle = "5.0mm",
            yarn = null,
            extra = null,
            price = 0,
            pattern = "# Step1. 코를 10개 잡습니다.",
            createdAt = LocalDateTime.now(),
        ).toDesign()

        val routerFunction = DesignRouter(DesignHandler(repo)).designRouterFunction()
        webClient = WebTestClient.bindToRouterFunction(routerFunction).build()
    }

    @Test
    fun `design 이 잘 생성되어야 함`() {
        val body = objectMapper.writeValueAsString(design)
        given(repo.createDesign(any())).willReturn(Mono.just(design))
        val response = webClient
            .post()
            .uri("/design/")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Design::class.java)
            .returnResult()
            .responseBody!!
        assertThat(response.id).isEqualTo(design.id)
        assertThat(response.name).isEqualTo("test")
        assertThat(response.designType).isEqualTo(DesignType.Sweater)
        assertThat(response.patternType).isEqualTo(PatternType.Text)
        assertThat(response.gauge.stitches).isEqualTo(23.5)
        assertThat(response.gauge.rows).isEqualTo(25.0)
        assertThat(response.needle).isEqualTo("5.0mm")
        assertThat(response.yarn).isEqualTo(null)
        assertThat(response.extra).isEqualTo(null)
        assertThat(response.price.value).isEqualTo(0)
        assertThat(response.size.totalLength.value).isEqualTo(1.0)
        assertThat(response.size.sleeveLength.value).isEqualTo(2.0)
        assertThat(response.size.shoulderWidth.value).isEqualTo(3.0)
        assertThat(response.size.bottomWidth.value).isEqualTo(4.0)
        assertThat(response.size.armholeDepth.value).isEqualTo(5.0)
        assertThat(response.pattern.value).isEqualTo("# Step1. 코를 10개 잡습니다.")
        assertThat(response.createdAt).isEqualTo(design.createdAt)
    }
}
