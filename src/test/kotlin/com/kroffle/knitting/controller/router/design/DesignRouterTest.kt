package com.kroffle.knitting.controller.router.design

import com.fasterxml.jackson.databind.ObjectMapper
import com.kroffle.knitting.controller.filter.auth.AuthorizationFilter
import com.kroffle.knitting.controller.handler.design.DesignHandler
import com.kroffle.knitting.controller.handler.design.dto.NewDesignRequest
import com.kroffle.knitting.controller.handler.design.dto.NewDesignResponse
import com.kroffle.knitting.controller.handler.design.dto.NewDesignSize
import com.kroffle.knitting.controller.handler.helper.response.type.APIResponse
import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.design.enum.DesignType
import com.kroffle.knitting.domain.design.enum.PatternType
import com.kroffle.knitting.infra.jwt.TokenDecoder
import com.kroffle.knitting.infra.jwt.TokenPublisher
import com.kroffle.knitting.infra.persistence.design.entity.DesignEntity
import com.kroffle.knitting.infra.properties.WebApplicationProperties
import com.kroffle.knitting.usecase.design.DesignRepository
import com.kroffle.knitting.usecase.design.DesignService
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
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Mono

@WebFluxTest
@ExtendWith(SpringExtension::class)
class DesignRouterTest {
    private lateinit var webClient: WebTestClient

    private lateinit var design: Design

    private lateinit var tokenPublisher: TokenPublisher

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var repo: DesignRepository

    @MockBean
    lateinit var tokenDecoder: TokenDecoder

    @MockBean
    private lateinit var webProperties: WebApplicationProperties

    private val secretKey = "I'M SECRET KEY!"

    @BeforeEach
    fun setUp() {
        tokenPublisher = TokenPublisher(secretKey)
        tokenDecoder = TokenDecoder(secretKey)

        design = DesignEntity(
            id = 1,
            knitterId = 1,
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
            yarn = "캐시미어 400g",
            extra = null,
            price = 0,
            pattern = "# Step1. 코를 10개 잡습니다.",
        ).toDesign()

        val routerFunction = DesignRouter(DesignHandler(DesignService(repo))).designRouterFunction()
        webClient = WebTestClient
            .bindToRouterFunction(routerFunction)
            .webFilter<WebTestClient.RouterFunctionSpec>(AuthorizationFilter(tokenDecoder))
            .build()
    }

    @Test
    fun `design 이 잘 생성되어야 함`() {
        val userId: Long = 1
        val token = tokenPublisher.publish(userId)

        val body = objectMapper.writeValueAsString(
            NewDesignRequest(
                name = "test",
                designType = DesignType.Sweater,
                patternType = PatternType.Text,
                stitches = 23.5,
                rows = 25.0,
                size = NewDesignSize(
                    totalLength = 1.0,
                    sleeveLength = 2.0,
                    shoulderWidth = 3.0,
                    bottomWidth = 4.0,
                    armholeDepth = 5.0,
                ),
                needle = "5.0mm",
                yarn = "캐시미어 400g",
                extra = null,
                price = 0,
                pattern = "# Step1. 코를 10개 잡습니다.",
            )
        )
        given(repo.createDesign(any())).willReturn(Mono.just(design))
        val response: APIResponse<NewDesignResponse> = webClient
            .post()
            .uri("/design/")
            .header("Authorization", "Bearer $token")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody<APIResponse<NewDesignResponse>>()
            .returnResult()
            .responseBody!!
        assertThat(response.data.id).isEqualTo(design.id)
    }
}
