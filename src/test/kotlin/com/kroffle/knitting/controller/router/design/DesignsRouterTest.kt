package com.kroffle.knitting.controller.router.design

import com.kroffle.knitting.controller.filter.auth.AuthorizationFilter
import com.kroffle.knitting.controller.handler.design.DesignHandler
import com.kroffle.knitting.controller.handler.design.dto.MyDesign
import com.kroffle.knitting.controller.handler.design.dto.MyDesignsResponse
import com.kroffle.knitting.controller.handler.design.dto.SalesSummaryResponse
import com.kroffle.knitting.domain.design.enum.DesignType
import com.kroffle.knitting.domain.design.enum.PatternType
import com.kroffle.knitting.infra.design.entity.DesignEntity
import com.kroffle.knitting.infra.jwt.TokenDecoder
import com.kroffle.knitting.infra.jwt.TokenPublisher
import com.kroffle.knitting.infra.properties.WebApplicationProperties
import com.kroffle.knitting.usecase.design.DesignRepository
import com.kroffle.knitting.usecase.design.DesignService
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

@WebFluxTest
@ExtendWith(SpringExtension::class)
class DesignsRouterTest {

    private lateinit var webClient: WebTestClient

    private lateinit var tokenPublisher: TokenPublisher

    private lateinit var token: String

    @MockBean
    lateinit var repo: DesignRepository

    @MockBean
    lateinit var tokenDecoder: AuthorizationFilter.TokenDecoder

    @MockBean
    private lateinit var webProperties: WebApplicationProperties

    private val secretKey = "I'M SECRET KEY!"

    private val userId: Long = 1

    @BeforeEach
    fun setUp() {
        tokenPublisher = TokenPublisher(secretKey)
        tokenDecoder = TokenDecoder(secretKey)

        token = tokenPublisher.publish(userId)

        val routerFunction = DesignsRouter(DesignHandler(DesignService(repo))).designsRouterFunction()
        webClient = WebTestClient
            .bindToRouterFunction(routerFunction)
            .webFilter<WebTestClient.RouterFunctionSpec>(AuthorizationFilter(tokenDecoder))
            .build()
    }

    @Test
    fun `내가 만든 도안 리스트가 잘 반환되어야 함`() {
        given(repo.getDesignsByKnitterId(1))
            .willReturn(
                Flux.just(
                    DesignEntity(
                        id = 1,
                        knitterId = 1,
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
                        price = 0,
                        pattern = "# Step1. 코를 10개 잡습니다.",
                    ).toDesign()
                )
            )

        val responseBody: MyDesignsResponse = webClient
            .get()
            .uri("/designs/my")
            .header("Authorization", "Bearer $token")
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
                    id = 1,
                    name = "캔디리더 효정 니트",
                    yarn = "패션아란 400g 1볼",
                    thumbnailImageUrl = null,
                    tags = listOf("니트", "서술형도안"),
                ),
            )
        )
    }

    @Test
    fun `나의 판매 요약 정보가 잘 반환되어야 함`() {
        val responseBody: SalesSummaryResponse = webClient
            .get()
            .uri("/designs/sales-summary/my")
            .header("Authorization", "Bearer $token")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(SalesSummaryResponse::class.java)
            .returnResult()
            .responseBody!!

        assertThat(responseBody).isEqualTo(
            SalesSummaryResponse(
                numberOfDesignsOnSales = 1,
                numberOfDesignsSold = 2,
            ),
        )
    }
}
