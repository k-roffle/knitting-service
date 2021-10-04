package com.kroffle.knitting.controller.router.product

import com.fasterxml.jackson.databind.ObjectMapper
import com.kroffle.knitting.controller.handler.product.ProductHandler
import com.kroffle.knitting.controller.handler.product.dto.EditProductContent
import com.kroffle.knitting.controller.handler.product.dto.EditProductPackage
import com.kroffle.knitting.controller.handler.product.dto.GetMyProduct
import com.kroffle.knitting.controller.handler.product.dto.GetMyProducts
import com.kroffle.knitting.controller.handler.product.dto.RegisterProduct
import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.domain.product.enum.InputStatus
import com.kroffle.knitting.domain.product.enum.ProductItemType
import com.kroffle.knitting.domain.product.value.ProductItem
import com.kroffle.knitting.domain.product.value.ProductTag
import com.kroffle.knitting.domain.value.Money
import com.kroffle.knitting.helper.MockFactory
import com.kroffle.knitting.helper.TestResponse
import com.kroffle.knitting.helper.WebTestClientHelper
import com.kroffle.knitting.helper.dto.MockProductData
import com.kroffle.knitting.helper.extension.addDefaultRequestHeader
import com.kroffle.knitting.helper.extension.like
import com.kroffle.knitting.infra.jwt.TokenDecoder
import com.kroffle.knitting.infra.properties.WebApplicationProperties
import com.kroffle.knitting.usecase.helper.pagination.type.SortDirection
import com.kroffle.knitting.usecase.product.ProductService
import com.kroffle.knitting.usecase.repository.ProductRepository
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
class ProductRouterTest {
    private lateinit var webClient: WebTestClient

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var repository: ProductRepository

    @MockBean
    private lateinit var tokenDecoder: TokenDecoder

    @MockBean
    private lateinit var webProperties: WebApplicationProperties

    private val today = OffsetDateTime.now()

    private val tomorrow = today.plusDays(1)

    private val yesterday = today.minusDays(1)

    @BeforeEach
    fun setUp() {
        webClient = WebTestClientHelper.createWebTestClient(
            ProductRouter(ProductHandler(ProductService(repository)))
                .productRouterFunction()
        )
    }

    @Test
    fun `상품 구성을 저장할 수 있어야 함`() {
        val createdProduct = Product(
            id = 1,
            knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
            name = "상품 이름",
            fullPrice = Money(1000),
            discountPrice = Money(1000),
            representativeImageUrl = "http://test.knitting.com/image.jpg",
            specifiedSalesStartDate = null,
            specifiedSalesEndDate = tomorrow.toLocalDate(),
            content = null,
            inputStatus = InputStatus.DRAFT,
            tags = listOf(
                ProductTag("서술형도안"),
                ProductTag("초보자용"),
            ),
            items = listOf(
                ProductItem.create(1, ProductItemType.DESIGN),
            ),
            createdAt = today,
            updatedAt = today,
        )
        given(repository.save(any())).willReturn(Mono.just(createdProduct))

        val body = objectMapper
            .writeValueAsString(
                EditProductPackage.Request(
                    id = null,
                    name = "상품 이름",
                    fullPrice = 1000,
                    discountPrice = 1000,
                    representativeImageUrl = "http://test.knitting.com/image.jpg",
                    specifiedSalesStartDate = null,
                    specifiedSalesEndDate = tomorrow.toLocalDate(),
                    tags = listOf("서술형도안", "초보자용"),
                    designIds = listOf(1),
                )
            )

        val response = webClient
            .post()
            .uri("/product/package/")
            .addDefaultRequestHeader()
            .bodyValue(body)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody<TestResponse<EditProductPackage.Response>>()
            .returnResult()
            .responseBody!!

        assertThat(response.payload.id).isEqualTo(createdProduct.id)
        verify(repository).save(
            argThat { product ->
                assert(
                    product.knitterId == createdProduct.knitterId &&
                        product.name == createdProduct.name &&
                        product.fullPrice.like(createdProduct.fullPrice) &&
                        product.discountPrice.like(createdProduct.discountPrice) &&
                        product.representativeImageUrl == createdProduct.representativeImageUrl &&
                        product.specifiedSalesStartDate == createdProduct.specifiedSalesStartDate &&
                        product.specifiedSalesEndDate == createdProduct.specifiedSalesEndDate &&
                        product.content == createdProduct.content &&
                        product.inputStatus == createdProduct.inputStatus
                )
                product.tags.mapIndexed { index, tag ->
                    assert(createdProduct.tags[index].name == tag.name)
                }
                product.items.mapIndexed { index, item ->
                    assert(createdProduct.items[index].itemId == item.itemId)
                }
                true
            }
        )
    }

    @Test
    fun `상품 구성을 수정할 수 있어야 함`() {
        val targetProduct = Product(
            id = 1,
            knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
            name = "상품 이름",
            fullPrice = Money(1000),
            discountPrice = Money(1000),
            representativeImageUrl = "http://test.knitting.com/image.jpg",
            specifiedSalesStartDate = null,
            specifiedSalesEndDate = tomorrow.toLocalDate(),
            content = null,
            inputStatus = InputStatus.DRAFT,
            tags = listOf(
                ProductTag("서술형도안"),
                ProductTag("초보자용"),
            ),
            items = listOf(
                ProductItem.create(1, ProductItemType.DESIGN),
            ),
            createdAt = today,
            updatedAt = today,
        )
        val updatedProduct = targetProduct
            .edit(
                knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
                name = "바뀐 상품 이름",
                fullPrice = Money(2000),
                discountPrice = Money(2000),
                representativeImageUrl = "http://test2.knitting.com/image.jpg",
                specifiedSalesStartDate = tomorrow.toLocalDate(),
                specifiedSalesEndDate = null,
                tags = listOf(ProductTag("서술형도안")),
                items = listOf(ProductItem.create(2, ProductItemType.DESIGN))
            )
        given(repository.getProductByIdAndKnitterId(any(), any()))
            .willReturn(Mono.just(targetProduct))
        given(repository.save(any())).willReturn(Mono.just(updatedProduct))

        val body = objectMapper
            .writeValueAsString(
                EditProductPackage.Request(
                    id = targetProduct.id!!,
                    name = "바뀐 상품 이름",
                    fullPrice = 2000,
                    discountPrice = 2000,
                    representativeImageUrl = "http://test2.knitting.com/image.jpg",
                    specifiedSalesStartDate = tomorrow.toLocalDate(),
                    specifiedSalesEndDate = null,
                    tags = listOf("서술형도안"),
                    designIds = listOf(2),
                )
            )

        val response = webClient
            .post()
            .uri("/product/package/")
            .addDefaultRequestHeader()
            .bodyValue(body)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody<TestResponse<EditProductPackage.Response>>()
            .returnResult()
            .responseBody!!

        assertThat(response.payload.id)
            .isEqualTo(targetProduct.id)
            .isEqualTo(updatedProduct.id)

        verify(repository)
            .getProductByIdAndKnitterId(
                WebTestClientHelper.AUTHORIZED_KNITTER_ID,
                targetProduct.id!!,
            )

        verify(repository).save(
            argThat { product ->
                assert(
                    product.knitterId == updatedProduct.knitterId &&
                        product.name == updatedProduct.name &&
                        product.fullPrice.like(updatedProduct.fullPrice) &&
                        product.discountPrice.like(updatedProduct.discountPrice) &&
                        product.representativeImageUrl == updatedProduct.representativeImageUrl &&
                        product.specifiedSalesStartDate == updatedProduct.specifiedSalesStartDate &&
                        product.specifiedSalesEndDate == updatedProduct.specifiedSalesEndDate &&
                        product.content == updatedProduct.content &&
                        product.inputStatus == updatedProduct.inputStatus &&
                        product.id == updatedProduct.id &&
                        product.createdAt == updatedProduct.createdAt
                )
                product.tags.mapIndexed {
                    index, tag ->
                    assert(updatedProduct.tags[index].name == tag.name)
                }
                product.items.mapIndexed {
                    index, item ->
                    assert(updatedProduct.items[index].itemId == item.itemId)
                }
                true
            }
        )
    }

    @Test
    fun `상품 설명을 저장할 수 있어야 함`() {
        val targetProduct = Product(
            id = 1,
            knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
            name = "상품 이름",
            fullPrice = Money(1000),
            discountPrice = Money(1000),
            representativeImageUrl = "http://test.knitting.com/image.jpg",
            specifiedSalesStartDate = null,
            specifiedSalesEndDate = tomorrow.toLocalDate(),
            content = null,
            inputStatus = InputStatus.DRAFT,
            tags = listOf(
                ProductTag("서술형도안"),
                ProductTag("초보자용"),
            ),
            items = listOf(
                ProductItem.create(1, ProductItemType.DESIGN),
            ),
            createdAt = yesterday,
            updatedAt = yesterday,
        )
        val updatedProduct = targetProduct.edit("상품 설명")

        given(repository.getProductByIdAndKnitterId(any(), any()))
            .willReturn(Mono.just(targetProduct))
        given(repository.save(any()))
            .willReturn(Mono.just(updatedProduct))

        val body = objectMapper
            .writeValueAsString(
                EditProductContent.Request(
                    id = 1,
                    content = "상품 설명",
                )
            )

        val response = webClient
            .post()
            .uri("/product/content/")
            .addDefaultRequestHeader()
            .bodyValue(body)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody<TestResponse<EditProductContent.Response>>()
            .returnResult()
            .responseBody!!

        assertThat(response.payload.id).isEqualTo(targetProduct.id)
        verify(repository)
            .getProductByIdAndKnitterId(
                1,
                WebTestClientHelper.AUTHORIZED_KNITTER_ID,
            )
        verify(repository)
            .save(
                argThat {
                    product ->
                    product.like(updatedProduct)
                    assert(product.updatedAt!! > yesterday)
                    true
                }
            )
    }

    @Test
    fun `상품을 판매 등록 할 수 있어야 함`() {
        val targetProduct = Product(
            id = 1,
            knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
            name = "상품 이름",
            fullPrice = Money(1000),
            discountPrice = Money(1000),
            representativeImageUrl = "http://test.knitting.com/image.jpg",
            specifiedSalesStartDate = null,
            specifiedSalesEndDate = null,
            content = "이번에는 초보탈출 패키지를 준비해봤어요.",
            inputStatus = InputStatus.DRAFT,
            tags = listOf(
                ProductTag("서술형도안"),
                ProductTag("초보자용"),
            ),
            items = listOf(
                ProductItem.create(1, ProductItemType.DESIGN),
            ),
            createdAt = yesterday,
            updatedAt = yesterday,
        )
        val updatedProduct = targetProduct.register()

        given(repository.getProductByIdAndKnitterId(any(), any()))
            .willReturn(Mono.just(targetProduct))
        given(repository.save(any()))
            .willReturn(Mono.just(updatedProduct))

        val body = objectMapper
            .writeValueAsString(
                RegisterProduct.Request(id = 1)
            )

        val response = webClient
            .post()
            .uri("/product")
            .addDefaultRequestHeader()
            .bodyValue(body)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody<TestResponse<RegisterProduct.Response>>()
            .returnResult()
            .responseBody!!

        assertThat(response.payload.id).isEqualTo(targetProduct.id)
        verify(repository)
            .getProductByIdAndKnitterId(
                1,
                WebTestClientHelper.AUTHORIZED_KNITTER_ID,
            )
        verify(repository)
            .save(
                argThat {
                    product ->
                    product.like(updatedProduct)
                    assert(product.updatedAt!! > yesterday)
                    true
                }
            )
    }

    @Test
    fun `내 상품을 조회할 수 있어야 함`() {
        val mockData = MockProductData(id = 1)
        val targetProduct = MockFactory.create(mockData)
        given(repository.getProductByIdAndKnitterId(any(), any()))
            .willReturn(Mono.just(targetProduct))

        val response = webClient
            .get()
            .uri("/product/mine/1")
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
                it.like(
                    GetMyProduct.Response(
                        id = mockData.id,
                        name = mockData.name,
                        fullPrice = mockData.fullPrice.value,
                        discountPrice = mockData.discountPrice.value,
                        representativeImageUrl = mockData.representativeImageUrl,
                        specifiedSalesStartDate = mockData.specifiedSalesStartDate,
                        specifiedSalesEndDate = mockData.specifiedSalesEndDate,
                        tags = mockData.tags.map { tag -> tag.name },
                        content = mockData.content,
                        inputStatus = mockData.inputStatus,
                        itemIds = mockData.items.map { item -> item.itemId },
                        createdAt = mockData.createdAt,
                        updatedAt = mockData.updatedAt,
                    )
                )
            }
        verify(repository)
            .getProductByIdAndKnitterId(
                1,
                WebTestClientHelper.AUTHORIZED_KNITTER_ID,
            )
    }

    @Test
    fun `내 상품 리스트를 조회할 수 있어야 함`() {
        val mockData = listOf(
            MockProductData(id = 1, updatedAt = today.minusDays(1)),
            MockProductData(id = 3, updatedAt = today.minusDays(2)),
            MockProductData(id = 2, updatedAt = today.minusDays(3)),
        )

        val mockProducts: List<Product> = mockData.map { MockFactory.create(it) }
        given(repository.getProductsByKnitterId(any(), any(), any()))
            .willReturn(Flux.fromIterable(mockProducts))

        val response = webClient
            .get()
            .uri("/product/mine")
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
                .like(
                    GetMyProducts.Response(
                        id = firstMockData.id,
                        name = firstMockData.name,
                        fullPrice = firstMockData.fullPrice.value,
                        discountPrice = firstMockData.discountPrice.value,
                        representativeImageUrl = firstMockData.representativeImageUrl,
                        specifiedSalesStartDate = firstMockData.specifiedSalesStartDate,
                        specifiedSalesEndDate = firstMockData.specifiedSalesEndDate,
                        inputStatus = firstMockData.inputStatus,
                        updatedAt = firstMockData.updatedAt,
                        tags = firstMockData.tags.map { tag -> tag.name }
                    )
                )
        )
        assertThat(payload[1].id).isEqualTo(3)
        assertThat(payload[2].id).isEqualTo(2)
        verify(repository).getProductsByKnitterId(
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
        given(repository.getProductsByKnitterId(any(), any(), any()))
            .willReturn(Flux.empty())

        webClient
            .get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/product/mine")
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

        verify(repository).getProductsByKnitterId(
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
