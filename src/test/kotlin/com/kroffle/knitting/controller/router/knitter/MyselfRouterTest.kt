package com.kroffle.knitting.controller.router.knitter

import com.kroffle.knitting.controller.handler.knitter.MyselfHandler
import com.kroffle.knitting.controller.handler.knitter.dto.MyProfileResponse
import com.kroffle.knitting.controller.handler.knitter.dto.SalesSummaryResponse
import com.kroffle.knitting.domain.product.enum.InputStatus
import com.kroffle.knitting.helper.MockFactory
import com.kroffle.knitting.helper.TestResponse
import com.kroffle.knitting.helper.WebTestClientHelper
import com.kroffle.knitting.helper.dto.MockProductData
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
        given(knitterRepository.findById(WebTestClientHelper.AUTHORIZED_KNITTER_ID))
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
            .expectBody<TestResponse<MyProfileResponse>>()
            .returnResult()
            .responseBody!!

        assertThat(result.payload).isEqualTo(
            MyProfileResponse(
                name = "홍길동",
                email = "test@test.com",
                profileImageUrl = null,
            ),
        )
    }

    @Test
    fun `나의 판매 요약 정보가 잘 반환되어야 함`() {
        val yesterday = OffsetDateTime.now().minusDays(1).toLocalDate()
        val productsToBeCounted = Flux.just(
            MockFactory.create(MockProductData(id = 1, content = "이 상품은요", inputStatus = InputStatus.REGISTERED)),
            MockFactory.create(
                MockProductData(
                    id = 2,
                    content = "이 상품은요",
                    inputStatus = InputStatus.REGISTERED,
                    specifiedSalesStartDate = yesterday,
                )
            ),
        )
        val productsToBeSkipped = Flux.just(
            MockFactory.create(MockProductData(id = 3, inputStatus = InputStatus.DRAFT)),
            MockFactory.create(MockProductData(id = 4, content = "이 상품은요", inputStatus = InputStatus.DRAFT)),
            MockFactory.create(
                MockProductData(
                    id = 5,
                    content = "이 상품은요",
                    inputStatus = InputStatus.REGISTERED,
                    specifiedSalesEndDate = yesterday,
                )
            ),

        )
        given(productRepository.findRegisteredProduct(any()))
            .willReturn(productsToBeCounted.concatWith(productsToBeSkipped))

        val responseBody: TestResponse<SalesSummaryResponse> = webClient
            .get()
            .uri("/me/sales-summary")
            .addDefaultRequestHeader()
            .exchange()
            .expectStatus()
            .isOk
            .expectBody<TestResponse<SalesSummaryResponse>>()
            .returnResult()
            .responseBody!!

        assertThat(responseBody.payload).isEqualTo(
            SalesSummaryResponse(
                numberOfProductsOnSales = 2,
                numberOfProductsSold = 0,
            ),
        )
        verify(productRepository)
            .findRegisteredProduct(WebTestClientHelper.AUTHORIZED_KNITTER_ID)
    }
}
