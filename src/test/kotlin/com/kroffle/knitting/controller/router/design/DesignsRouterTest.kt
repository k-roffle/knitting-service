package com.kroffle.knitting.controller.router.design

import com.kroffle.knitting.controller.handler.design.DesignHandler
import com.kroffle.knitting.controller.handler.design.dto.MyDesign
import com.kroffle.knitting.domain.design.enum.DesignType
import com.kroffle.knitting.domain.design.enum.LevelType
import com.kroffle.knitting.domain.design.enum.PatternType
import com.kroffle.knitting.helper.TestResponse
import com.kroffle.knitting.helper.WebTestClientHelper
import com.kroffle.knitting.helper.extension.addDefaultRequestHeader
import com.kroffle.knitting.helper.extension.like
import com.kroffle.knitting.infra.jwt.TokenDecoder
import com.kroffle.knitting.infra.persistence.design.entity.DesignEntity
import com.kroffle.knitting.infra.properties.WebApplicationProperties
import com.kroffle.knitting.usecase.repository.DesignRepository
import com.kroffle.knitting.usecase.design.DesignService
import com.kroffle.knitting.usecase.helper.pagination.type.SortDirection
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.verify
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Flux
import java.time.LocalDateTime

@WebFluxTest
@ExtendWith(SpringExtension::class)
class DesignsRouterTest {
    private lateinit var webClient: WebTestClient

    @MockBean
    private lateinit var repository: DesignRepository

    @MockBean
    private lateinit var tokenDecoder: TokenDecoder

    @MockBean
    private lateinit var webProperties: WebApplicationProperties

    @BeforeEach
    fun setUp() {
        webClient = WebTestClientHelper.createWebTestClient(
            DesignsRouter(DesignHandler(DesignService(repository)))
                .designsRouterFunction()
        )
    }

    @Test
    fun `내가 만든 도안 리스트가 잘 반환되어야 함`() {
        val today: LocalDateTime = LocalDateTime.now()
        given(repository.getDesignsByKnitterId(any(), any(), any()))
            .willReturn(
                Flux.just(
                    DesignEntity(
                        id = 1,
                        knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
                        name = "캔디리더 효정 니트",
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
                        yarn = "패션아란 400g 1볼",
                        extra = null,
                        pattern = "# Step1. 코를 10개 잡습니다.",
                        description = "이건 니트를 만드는 서술형 도안입니다.",
                        targetLevel = LevelType.HARD.key,
                        coverImageUrl = "http://test.kroffle.com/image.jpg",
                        createdAt = today,
                    ).toDesign()
                )
            )

        val responseBody: TestResponse<List<MyDesign>> =
            webClient
                .get()
                .uri("/designs/my")
                .addDefaultRequestHeader()
                .exchange()
                .expectStatus()
                .isOk
                .expectBody<TestResponse<List<MyDesign>>>()
                .returnResult()
                .responseBody!!

        assertThat(responseBody.payload.size).isEqualTo(1)
        assert(
            responseBody.payload.first().like(
                MyDesign(
                    id = 1,
                    name = "캔디리더 효정 니트",
                    yarn = "패션아란 400g 1볼",
                    coverImageUrl = "http://test.kroffle.com/image.jpg",
                    tags = listOf("니트", "서술형도안"),
                    createdAt = today,
                ),
            )
        )
        verify(repository).getDesignsByKnitterId(
            argThat { param -> param == 1.toLong() },
            argThat {
                param ->
                assert(param.after == null)
                assert(param.count == 10)
                true
            },
            argThat {
                param ->
                assert(param.column == "id")
                assert(param.direction == SortDirection.DESC)
                true
            },
        )
    }
}
