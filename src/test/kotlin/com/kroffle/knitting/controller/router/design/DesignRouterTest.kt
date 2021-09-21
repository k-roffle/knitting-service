package com.kroffle.knitting.controller.router.design

import com.fasterxml.jackson.databind.ObjectMapper
import com.kroffle.knitting.controller.handler.design.DesignHandler
import com.kroffle.knitting.controller.handler.design.dto.NewDesignRequest
import com.kroffle.knitting.controller.handler.design.dto.NewDesignResponse
import com.kroffle.knitting.controller.handler.design.dto.NewDesignSize
import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.design.enum.DesignType
import com.kroffle.knitting.domain.design.enum.LevelType
import com.kroffle.knitting.domain.design.enum.PatternType
import com.kroffle.knitting.domain.design.value.Gauge
import com.kroffle.knitting.domain.design.value.Length
import com.kroffle.knitting.domain.design.value.Pattern
import com.kroffle.knitting.domain.design.value.Size
import com.kroffle.knitting.domain.design.value.Technique
import com.kroffle.knitting.helper.TestResponse
import com.kroffle.knitting.helper.WebTestClientHelper
import com.kroffle.knitting.helper.extension.addDefaultRequestHeader
import com.kroffle.knitting.helper.extension.like
import com.kroffle.knitting.infra.jwt.TokenDecoder
import com.kroffle.knitting.infra.properties.WebApplicationProperties
import com.kroffle.knitting.usecase.design.DesignService
import com.kroffle.knitting.usecase.repository.DesignRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Mono
import java.time.OffsetDateTime

@WebFluxTest
@ExtendWith(SpringExtension::class)
class DesignRouterTest {
    private lateinit var webClient: WebTestClient

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var repository: DesignRepository

    @MockBean
    private lateinit var tokenDecoder: TokenDecoder

    @MockBean
    private lateinit var webProperties: WebApplicationProperties

    @BeforeEach
    fun setUp() {
        webClient = WebTestClientHelper.createWebTestClient(
            DesignRouter(DesignHandler(DesignService(repository)))
                .designRouterFunction()
        )
    }

    @Test
    fun `design 이 잘 생성되어야 함`() {
        val createdDesign = Design(
            id = 1,
            knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
            name = "test",
            designType = DesignType.Sweater,
            patternType = PatternType.Text,
            gauge = Gauge(
                stitches = 23.5,
                rows = 25.0,
            ),
            size = Size(
                totalLength = Length(1.0),
                sleeveLength = Length(2.0),
                shoulderWidth = Length(3.0),
                bottomWidth = Length(4.0),
                armholeDepth = Length(5.0),
            ),
            needle = "5.0mm",
            yarn = "캐시미어 400g",
            extra = null,
            pattern = Pattern("# Step1. 코를 10개 잡습니다."),
            description = "이건 니트를 만드는 서술형 도안입니다.",
            targetLevel = LevelType.HARD,
            coverImageUrl = "http://test.kroffle.com/image.jpg",
            techniques = listOf(Technique("겉뜨기"), Technique("안뜨기")),
            createdAt = OffsetDateTime.now(),
        )
        given(repository.createDesign(any())).willReturn(Mono.just(createdDesign))

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
                pattern = "# Step1. 코를 10개 잡습니다.",
                description = "이건 니트를 만드는 서술형 도안입니다.",
                targetLevel = LevelType.HARD,
                coverImageUrl = "http://test.kroffle.com/image.jpg",
                techniques = listOf("겉뜨기", "안뜨기")
            )
        )
        val response: TestResponse<NewDesignResponse> =
            webClient
                .post()
                .uri("/design/")
                .addDefaultRequestHeader()
                .bodyValue(body)
                .exchange()
                .expectStatus()
                .isOk
                .expectBody<TestResponse<NewDesignResponse>>()
                .returnResult()
                .responseBody!!

        assertThat(response.payload.id).isEqualTo(createdDesign.id)
        verify(repository).createDesign(
            argThat {
                design ->
                assert(
                    design.knitterId == createdDesign.knitterId &&
                        design.name == createdDesign.name &&
                        design.designType == createdDesign.designType &&
                        design.patternType == createdDesign.patternType &&
                        design.gauge.like(createdDesign.gauge) &&
                        design.size.like(createdDesign.size) &&
                        design.needle == createdDesign.needle &&
                        design.yarn == createdDesign.yarn &&
                        design.extra == createdDesign.extra &&
                        design.pattern.like(createdDesign.pattern) &&
                        design.description == createdDesign.description &&
                        design.targetLevel == createdDesign.targetLevel &&
                        design.coverImageUrl == createdDesign.coverImageUrl
                )
                true
            }
        )
    }
}
