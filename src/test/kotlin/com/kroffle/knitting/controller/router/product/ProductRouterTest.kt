package com.kroffle.knitting.controller.router.product

import com.fasterxml.jackson.databind.ObjectMapper
import com.kroffle.knitting.controller.handler.product.ProductHandler
import com.kroffle.knitting.controller.handler.product.dto.DraftProductContentRequest
import com.kroffle.knitting.controller.handler.product.dto.DraftProductContentResponse
import com.kroffle.knitting.controller.handler.product.dto.DraftProductPackageRequest
import com.kroffle.knitting.controller.handler.product.dto.DraftProductPackageResponse
import com.kroffle.knitting.controller.handler.product.dto.RegisterProductRequest
import com.kroffle.knitting.controller.handler.product.dto.RegisterProductResponse
import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.domain.product.enum.InputStatus
import com.kroffle.knitting.domain.product.enum.ProductItemType
import com.kroffle.knitting.domain.product.value.ProductItem
import com.kroffle.knitting.domain.product.value.ProductTag
import com.kroffle.knitting.domain.value.Money
import com.kroffle.knitting.helper.TestResponse
import com.kroffle.knitting.helper.WebTestClientHelper
import com.kroffle.knitting.helper.extension.addDefaultRequestHeader
import com.kroffle.knitting.helper.extension.like
import com.kroffle.knitting.infra.jwt.TokenDecoder
import com.kroffle.knitting.infra.properties.WebApplicationProperties
import com.kroffle.knitting.usecase.product.ProductRepository
import com.kroffle.knitting.usecase.product.ProductService
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
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.LocalDateTime

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

    @BeforeEach
    fun setUp() {
        webClient = WebTestClientHelper.createWebTestClient(
            ProductRouter(ProductHandler(ProductService(repository)))
                .productRouterFunction()
        )
    }

    @Test
    fun `상품 구성을 저장할 수 있어야 함`() {
        val today = LocalDateTime.now()
        val tomorrow = LocalDate.now().plusDays(1)
        val createdProduct = Product(
            id = 1,
            knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
            name = "상품 이름",
            fullPrice = Money(1000),
            discountPrice = Money(1000),
            representativeImageUrl = "http://test.knitting.com/image.jpg",
            specifiedSalesStartDate = null,
            specifiedSalesEndDate = tomorrow,
            content = null,
            inputStatus = InputStatus.DRAFT,
            tags = listOf(
                ProductTag(1, "서술형도안", today),
                ProductTag(2, "초보자용", today),
            ),
            items = listOf(
                ProductItem.create(1, 1, today, ProductItemType.DESIGN),
            ),
            createdAt = today,
        )
        given(repository.save(any())).willReturn(Mono.just(createdProduct))

        val body = objectMapper
            .writeValueAsString(
                DraftProductPackageRequest(
                    id = null,
                    name = "상품 이름",
                    fullPrice = 1000,
                    discountPrice = 1000,
                    representativeImageUrl = "http://test.knitting.com/image.jpg",
                    specifiedSalesStartDate = null,
                    specifiedSalesEndDate = tomorrow,
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
            .expectBody<TestResponse<DraftProductPackageResponse>>()
            .returnResult()
            .responseBody!!

        assertThat(response.payload.id).isEqualTo(createdProduct.id)
        verify(repository).save(
            argThat {
                product ->
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
                product.tags.mapIndexed {
                    index, tag ->
                    assert(createdProduct.tags[index].tag == tag.tag)
                }
                product.items.mapIndexed {
                    index, item ->
                    assert(createdProduct.items[index].itemId == item.itemId)
                }
                true
            }
        )
    }

    @Test
    fun `상품 설명을 저장할 수 있어야 함`() {
        val today = LocalDateTime.now()
        val tomorrow = LocalDate.now().plusDays(1)
        val targetProduct = Product(
            id = 1,
            knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
            name = "상품 이름",
            fullPrice = Money(1000),
            discountPrice = Money(1000),
            representativeImageUrl = "http://test.knitting.com/image.jpg",
            specifiedSalesStartDate = null,
            specifiedSalesEndDate = tomorrow,
            content = null,
            inputStatus = InputStatus.DRAFT,
            tags = listOf(
                ProductTag(1, "서술형도안", today),
                ProductTag(2, "초보자용", today),
            ),
            items = listOf(
                ProductItem.create(1, 1, today, ProductItemType.DESIGN),
            ),
            createdAt = today,
        )
        val updatedProduct = targetProduct.draftContent("상품 설명")

        given(repository.findById(any())).willReturn(Mono.just(targetProduct))
        given(repository.save(any())).willReturn(Mono.just(updatedProduct))

        val body = objectMapper
            .writeValueAsString(
                DraftProductContentRequest(
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
            .expectBody<TestResponse<DraftProductContentResponse>>()
            .returnResult()
            .responseBody!!

        assertThat(response.payload.id).isEqualTo(targetProduct.id)
        verify(repository).findById(1)
        verify(repository).save(
            argThat {
                product ->
                product.like(updatedProduct)
            }
        )
    }

    @Test
    fun `상품을 판매 등록 할 수 있어야 함`() {
        val today = LocalDateTime.now()
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
                ProductTag(1, "서술형도안", today),
                ProductTag(2, "초보자용", today),
            ),
            items = listOf(
                ProductItem.create(1, 1, today, ProductItemType.DESIGN),
            ),
            createdAt = today,
        )
        val updatedProduct = targetProduct.register()

        given(repository.findById(any())).willReturn(Mono.just(targetProduct))
        given(repository.save(any())).willReturn(Mono.just(updatedProduct))

        val body = objectMapper
            .writeValueAsString(
                RegisterProductRequest(id = 1)
            )

        val response = webClient
            .post()
            .uri("/product")
            .addDefaultRequestHeader()
            .bodyValue(body)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody<TestResponse<RegisterProductResponse>>()
            .returnResult()
            .responseBody!!

        assertThat(response.payload.id).isEqualTo(targetProduct.id)
        verify(repository).findById(1)
        verify(repository).save(
            argThat {
                product ->
                product.like(updatedProduct)
            }
        )
    }
}
