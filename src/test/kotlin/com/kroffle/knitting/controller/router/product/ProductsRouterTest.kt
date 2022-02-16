package com.kroffle.knitting.controller.router.product

import com.fasterxml.jackson.databind.ObjectMapper
import com.kroffle.knitting.common.extensions.onUTC
import com.kroffle.knitting.controller.handler.draftproduct.DraftProductHandler
import com.kroffle.knitting.controller.handler.product.ProductHandler
import com.kroffle.knitting.controller.handler.product.dto.GetMyProduct
import com.kroffle.knitting.controller.handler.product.dto.GetMyProducts
import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.helper.MockData
import com.kroffle.knitting.helper.MockFactory
import com.kroffle.knitting.helper.TestResponse
import com.kroffle.knitting.helper.WebTestClientHelper
import com.kroffle.knitting.helper.extension.addDefaultRequestHeader
import com.kroffle.knitting.infra.jwt.TokenDecoder
import com.kroffle.knitting.infra.properties.WebApplicationProperties
import com.kroffle.knitting.usecase.draftproduct.DraftProductService
import com.kroffle.knitting.usecase.helper.pagination.type.SortDirection
import com.kroffle.knitting.usecase.product.ProductService
import com.kroffle.knitting.usecase.repository.DraftProductRepository
import com.kroffle.knitting.usecase.repository.ProductRepository
import io.kotest.matchers.shouldBe
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.given
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
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
class ProductsRouterTest {
    private lateinit var webClient: WebTestClient

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var productRepository: ProductRepository

    @MockBean
    private lateinit var draftProductRepository: DraftProductRepository

    @MockBean
    private lateinit var tokenDecoder: TokenDecoder

    @MockBean
    private lateinit var webProperties: WebApplicationProperties

    private val today = OffsetDateTime.now().onUTC()

    private val tomorrow = today.plusDays(1)

    private val yesterday = today.minusDays(1)

    @BeforeEach
    fun setUp() {
        webClient = WebTestClientHelper.createWebTestClient(
            ProductsRouter(
                ProductHandler(
                    ProductService(productRepository, draftProductRepository),
                ),
                DraftProductHandler(
                    DraftProductService(draftProductRepository, productRepository)
                )
            )
                .productRouterFunction()
        )
    }

    @Test
    fun `내 상품을 조회할 수 있어야 함`() {
        val mockData = MockData.Product(id = 1)
        val targetProduct = MockFactory.create(mockData)
        given(productRepository.getProduct(any(), any()))
            .willReturn(Mono.just(targetProduct))

        val response = webClient
            .get()
            .uri("/products/mine/1")
            .addDefaultRequestHeader()
            .exchange()
            .expectStatus()
            .isOk
            .expectBody<TestResponse<GetMyProduct.Response>>()
            .returnResult()
            .responseBody!!

        val payload = response.payload
        assertThat(payload)
            .satisfies {
                it.shouldBe(
                    GetMyProduct.Response(
                        id = mockData.id,
                        name = mockData.name,
                        fullPrice = mockData.fullPrice.value,
                        discountPrice = mockData.discountPrice.value,
                        representativeImageUrl = mockData.representativeImageUrl,
                        specifiedSalesStartedAt = mockData.specifiedSalesStartedAt,
                        specifiedSalesEndedAt = mockData.specifiedSalesEndedAt,
                        tags = mockData.tags.map { tag -> tag.name },
                        content = mockData.content,
                        inputStatus = "inputStatus",
                        itemIds = mockData.items.map { item -> item.itemId },
                        createdAt = mockData.createdAt?.onUTC(),
                        updatedAt = mockData.updatedAt?.onUTC(),
                    )
                )
            }
        verify(productRepository)
            .getProduct(
                1,
                WebTestClientHelper.AUTHORIZED_KNITTER_ID,
            )
    }

    @Test
    fun `내 상품 리스트를 조회할 수 있어야 함`() {
        val mockData = listOf(
            MockData.Product(id = 1, updatedAt = today.minusDays(1)),
            MockData.Product(id = 3, updatedAt = today.minusDays(2)),
            MockData.Product(id = 2, updatedAt = today.minusDays(3)),
        )

        val mockProducts: List<Product> = mockData.map { MockFactory.create(it) }
        given(productRepository.getProductsByKnitterId(any(), any(), any()))
            .willReturn(Flux.fromIterable(mockProducts))

        val response = webClient
            .get()
            .uri("/products/mine")
            .addDefaultRequestHeader()
            .exchange()
            .expectStatus()
            .isOk
            .expectBody<TestResponse<List<GetMyProducts.Response>>>()
            .returnResult()
            .responseBody!!

        val payload = response.payload
        val firstMockData = mockData.first()
        assertThat(payload.size).isEqualTo(3)
        assertThat(
            payload
                .first()
                .shouldBe(
                    GetMyProducts.Response(
                        id = firstMockData.id,
                        name = firstMockData.name,
                        fullPrice = firstMockData.fullPrice.value,
                        discountPrice = firstMockData.discountPrice.value,
                        representativeImageUrl = firstMockData.representativeImageUrl,
                        specifiedSalesStartedAt = firstMockData.specifiedSalesStartedAt,
                        specifiedSalesEndedAt = firstMockData.specifiedSalesEndedAt,
                        inputStatus = "inputStatus",
                        updatedAt = firstMockData.updatedAt,
                        tags = firstMockData.tags.map { tag -> tag.name }
                    )
                )
        )
        assertThat(payload[1].id).isEqualTo(3)
        assertThat(payload[2].id).isEqualTo(2)
        verify(productRepository).getProductsByKnitterId(
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
    fun `내 상품 리스트를 더 불러올 때 페이지네이션 정보가 적절히 넘어가야 함`() {
        given(productRepository.getProductsByKnitterId(any(), any(), any()))
            .willReturn(Flux.empty())

        webClient
            .get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/products/mine")
                    .queryParam("count", "2")
                    .queryParam("after", "1")
                    .build()
            }
            .addDefaultRequestHeader()
            .exchange()
            .expectStatus()
            .isOk
            .expectBody<TestResponse<List<GetMyProducts.Response>>>()
            .returnResult()
            .responseBody!!

        verify(productRepository).getProductsByKnitterId(
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
