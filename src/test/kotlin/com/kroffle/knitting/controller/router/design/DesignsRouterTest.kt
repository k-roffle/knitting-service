package com.kroffle.knitting.controller.router.design

import com.kroffle.knitting.controller.handler.design.DesignHandler
import com.kroffle.knitting.controller.handler.design.dto.MyDesign
import com.kroffle.knitting.controller.handler.draftdesign.DraftDesignHandler
import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.design.value.Gauge
import com.kroffle.knitting.domain.design.value.Length
import com.kroffle.knitting.domain.design.value.Pattern
import com.kroffle.knitting.domain.design.value.Size
import com.kroffle.knitting.domain.design.value.Technique
import com.kroffle.knitting.domain.value.Money
import com.kroffle.knitting.helper.TestResponse
import com.kroffle.knitting.helper.WebTestClientHelper
import com.kroffle.knitting.helper.extension.addDefaultRequestHeader
import com.kroffle.knitting.helper.extension.like
import com.kroffle.knitting.infra.jwt.TokenDecoder
import com.kroffle.knitting.infra.properties.WebApplicationProperties
import com.kroffle.knitting.usecase.design.DesignService
import com.kroffle.knitting.usecase.draftdesign.DraftDesignService
import com.kroffle.knitting.usecase.helper.pagination.type.SortDirection
import com.kroffle.knitting.usecase.repository.DesignRepository
import com.kroffle.knitting.usecase.repository.DraftDesignRepository
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
import java.time.OffsetDateTime

@WebFluxTest
@ExtendWith(SpringExtension::class)
class DesignsRouterTest {
    private lateinit var webClient: WebTestClient

    @MockBean
    private lateinit var designRepository: DesignRepository

    @MockBean
    private lateinit var draftDesignRepository: DraftDesignRepository

    @MockBean
    private lateinit var tokenDecoder: TokenDecoder

    @MockBean
    private lateinit var webProperties: WebApplicationProperties

    @BeforeEach
    fun setUp() {
        webClient = WebTestClientHelper
            .createWebTestClient(
                DesignsRouter(
                    DesignHandler(DesignService(designRepository, draftDesignRepository)),
                    DraftDesignHandler(DraftDesignService(draftDesignRepository, designRepository)),
                )
                    .designsRouterFunction()
            )
    }

    @Test
    fun `내가 만든 도안 리스트가 잘 반환되어야 함`() {
        val today: OffsetDateTime = OffsetDateTime.now()
        given(designRepository.getDesignsByKnitterId(any(), any(), any()))
            .willReturn(
                Flux.just(
                    Design(
                        id = 1,
                        knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
                        name = "캔디리더 효정 니트",
                        designType = Design.DesignType.Sweater,
                        patternType = Design.PatternType.Text,
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
                        yarn = "패션아란 400g 1볼",
                        extra = null,
                        price = Money(1000),
                        pattern = Pattern("# Step1. 코를 10개 잡습니다."),
                        description = "이건 니트를 만드는 서술형 도안입니다.",
                        targetLevel = Design.LevelType.HARD,
                        coverImageUrl = "http://test.kroffle.com/image.jpg",
                        techniques = listOf(Technique("안뜨기"), Technique("겉뜨기")),
                        createdAt = today,
                    )
                )
            )

        val responseBody: TestResponse<List<MyDesign.Response>> =
            webClient
                .get()
                .uri("/designs/mine")
                .addDefaultRequestHeader()
                .exchange()
                .expectStatus()
                .isOk
                .expectBody<TestResponse<List<MyDesign.Response>>>()
                .returnResult()
                .responseBody!!

        assertThat(responseBody.payload.size).isEqualTo(1)
        assert(
            responseBody.payload.first().like(
                MyDesign.Response(
                    id = 1,
                    name = "캔디리더 효정 니트",
                    yarn = "패션아란 400g 1볼",
                    coverImageUrl = "http://test.kroffle.com/image.jpg",
                    tags = listOf("니트", "서술형도안"),
                    createdAt = today,
                ),
            )
        )
        verify(designRepository).getDesignsByKnitterId(
            argThat { param -> param == WebTestClientHelper.AUTHORIZED_KNITTER_ID },
            argThat { param ->
                assert(param.after == null)
                assert(param.count == 10)
                true
            },
            argThat { param ->
                assert(param.column == "id")
                assert(param.direction == SortDirection.DESC)
                true
            },
        )
    }

    @Test
    fun `내가 만든 도안 리스트를 더 불러올 때 페이지네이션 정보가 적절히 넘어가야 함`() {
        given(designRepository.getDesignsByKnitterId(any(), any(), any()))
            .willReturn(Flux.empty())

        webClient
            .get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/designs/mine")
                    .queryParam("count", "2")
                    .queryParam("after", "1")
                    .build()
            }
            .addDefaultRequestHeader()
            .exchange()
            .expectStatus()
            .isOk
            .expectBody<TestResponse<List<MyDesign>>>()
            .returnResult()
            .responseBody!!

        verify(designRepository).getDesignsByKnitterId(
            argThat { param -> param == WebTestClientHelper.AUTHORIZED_KNITTER_ID },
            argThat { param ->
                assert(param.after == "1")
                assert(param.count == 2)
                true
            },
            argThat { param ->
                assert(param.column == "id")
                assert(param.direction == SortDirection.DESC)
                true
            },
        )
    }
}
