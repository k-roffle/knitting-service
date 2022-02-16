package com.kroffle.knitting.controller.router.knitter

import com.kroffle.knitting.controller.handler.knitter.MyselfHandler
import com.kroffle.knitting.controller.handler.knitter.dto.MyProfile
import com.kroffle.knitting.controller.handler.knitter.dto.SalesSummary
import com.kroffle.knitting.helper.MockData
import com.kroffle.knitting.helper.MockFactory
import com.kroffle.knitting.helper.TestResponse
import com.kroffle.knitting.helper.WebTestClientHelper
import com.kroffle.knitting.helper.extension.addDefaultRequestHeader
import com.kroffle.knitting.infra.jwt.TokenDecoder
import com.kroffle.knitting.infra.jwt.TokenPublisher
import com.kroffle.knitting.infra.persistence.knitter.entity.KnitterEntity
import com.kroffle.knitting.infra.properties.WebApplicationProperties
import com.kroffle.knitting.usecase.auth.AuthService
import com.kroffle.knitting.usecase.knitter.KnitterService
import com.kroffle.knitting.usecase.repository.KnitterRepository
import com.kroffle.knitting.usecase.repository.ProductRepository
import com.kroffle.knitting.usecase.summary.ProductSummaryService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.OffsetDateTime

@WebFluxTest
@ExtendWith(SpringExtension::class)
class MyselfRouterTest {
    private lateinit var webClient: WebTestClient

    @MockBean
    private lateinit var knitterRepository: KnitterRepository

    @MockBean
    private lateinit var productRepository: ProductRepository

    @MockBean
    private lateinit var mockOAuthHelper: AuthService.OAuthHelper

    @MockBean
    private lateinit var tokenDecoder: TokenDecoder

    @MockBean
    private lateinit var tokenPublisher: TokenPublisher

    @MockBean
    private lateinit var webProperties: WebApplicationProperties

    @BeforeEach
    fun setUp() {
        webClient = WebTestClientHelper
            .createWebTestClient(
                MyselfRouter(
                    MyselfHandler(
                        KnitterService(knitterRepository),
                        ProductSummaryService(productRepository),
                    ),

                )
                    .profileRouterFunction()
            )
    }

    @Test
    fun `내 프로필을 조회할 수 있어야 함`() {
        given(knitterRepository.getById(WebTestClientHelper.AUTHORIZED_KNITTER_ID))
            .willReturn(
                Mono.just(
                    KnitterEntity(
                        id = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
                        name = "홍길동",
                        email = "test@test.com",
                        profileImageUrl = null,
                    ).toKnitter()
                )
            )

        val result = webClient
            .get()
            .uri("/me/profile")
            .addDefaultRequestHeader()
            .exchange()
            .expectStatus().isOk
            .expectBody<TestResponse<MyProfile.Response>>()
            .returnResult()
            .responseBody!!

        assertThat(result.payload).isEqualTo(
            MyProfile.Response(
                name = "홍길동",
                email = "test@test.com",
                profileImageUrl = null,
            ),
        )
    }

    @Test
    fun `나의 판매 요약 정보가 잘 반환되어야 함`() {
        val yesterday = OffsetDateTime.now().minusDays(1)
        val products = Flux.just(
            MockFactory.create(
                MockData.Product(
                    id = 1,
                    content = "이 상품은요",
                )
            ),
            MockFactory.create(
                MockData.Product(
                    id = 2,
                    content = "이 상품은요",
                    specifiedSalesStartedAt = yesterday,
                )
            ),
        )
        given(productRepository.findRegisteredProduct(any()))
            .willReturn(products)

        val responseBody: TestResponse<SalesSummary.Response> = webClient
            .get()
            .uri("/me/sales-summary")
            .addDefaultRequestHeader()
            .exchange()
            .expectStatus()
            .isOk
            .expectBody<TestResponse<SalesSummary.Response>>()
            .returnResult()
            .responseBody!!

        assertThat(responseBody.payload).isEqualTo(
            SalesSummary.Response(
                numberOfProductsOnSales = 2,
                numberOfProductsSold = 0,
            ),
        )
        verify(productRepository)
            .findRegisteredProduct(WebTestClientHelper.AUTHORIZED_KNITTER_ID)
    }
}
