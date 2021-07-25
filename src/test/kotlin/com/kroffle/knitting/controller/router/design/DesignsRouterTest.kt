package com.kroffle.knitting.controller.router.design

import com.kroffle.knitting.controller.filter.auth.AuthorizationFilter
import com.kroffle.knitting.controller.handler.design.DesignHandler
import com.kroffle.knitting.controller.handler.design.dto.MyDesign
import com.kroffle.knitting.controller.handler.design.dto.MyDesignsResponse
import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.infra.properties.WebApplicationProperties
import com.kroffle.knitting.usecase.design.DesignRepository
import com.kroffle.knitting.usecase.design.DesignService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest
@ExtendWith(SpringExtension::class)
class DesignsRouterTest {

    private lateinit var webClient: WebTestClient

    private lateinit var design: Design

    @MockBean
    lateinit var repo: DesignRepository

    @MockBean
    lateinit var tokenDecoder: AuthorizationFilter.TokenDecoder

    @MockBean
    private lateinit var webProperties: WebApplicationProperties

    @BeforeEach
    fun setUp() {
        val routerFunction = DesignsRouter(DesignHandler(DesignService(repo))).designsRouterFunction()
        webClient = WebTestClient.bindToRouterFunction(routerFunction).build()
    }

    @Test
    fun `내가 만든 도안 리스트가 잘 반환되어야 함`() {
        val responseBody: MyDesignsResponse = webClient
            .get()
            .uri("/designs/my")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(MyDesignsResponse::class.java)
            .returnResult()
            .responseBody!!

        assertThat(responseBody.designs).isEqualTo(
            listOf(
                MyDesign(
                    name = "캔디리더 효정 니트",
                    yarn = "패션아란 400g 1볼",
                    tags = listOf("니트", "서술형도안"),
                ),
                MyDesign(
                    name = "유샤샤 니트",
                    yarn = "캐시미어 300g 1볼",
                    tags = listOf("니트", "서술형도안"),
                ),
            )
        )
    }
}
