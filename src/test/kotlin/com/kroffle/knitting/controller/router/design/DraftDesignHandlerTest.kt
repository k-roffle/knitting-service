package com.kroffle.knitting.controller.router.design

import com.kroffle.knitting.controller.handler.draftdesign.DraftDesignHandler
import com.kroffle.knitting.controller.handler.draftdesign.dto.MyDraftDesign
import com.kroffle.knitting.controller.handler.draftdesign.dto.MyDraftDesigns
import com.kroffle.knitting.domain.draftdesign.entity.DraftDesign
import com.kroffle.knitting.helper.MockData
import com.kroffle.knitting.helper.MockFactory
import com.kroffle.knitting.helper.TestResponse
import com.kroffle.knitting.helper.WebTestClientHelper
import com.kroffle.knitting.helper.extension.addDefaultRequestHeader
import com.kroffle.knitting.infra.persistence.exception.NotFoundEntity
import com.kroffle.knitting.usecase.draftdesign.DraftDesignService
import io.kotest.core.spec.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.OffsetDateTime
import java.time.ZoneOffset

@DisplayName("DraftDesignHandler Test")
class DraftDesignHandlerTest : DescribeSpec() {
    init {
        val draftDesignService = mockk<DraftDesignService>()
        val router = DesignsRouter(mockk(), DraftDesignHandler(draftDesignService))
        val webClient = WebTestClientHelper
            .createWebTestClient(router.designsRouterFunction())

        afterContainer { clearAllMocks() }

        describe("내 작성중인 도안 리스트 조회 test") {
            val exchangeRequest = fun (): WebTestClient.ResponseSpec =
                webClient
                    .get()
                    .uri("/designs/draft/mine")
                    .addDefaultRequestHeader()
                    .exchange()

            context("작성 중인 도안이 있는 경우") {
                val updatedAt = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC)
                val draftDesigns = listOf(
                    MockFactory.create(
                        MockData.DraftDesign(
                            id = 1,
                            value = "{\"name\": \"작성 중\"}",
                            updatedAt = updatedAt,
                        )
                    ),
                    MockFactory.create(
                        MockData.DraftDesign(
                            id = 2,
                            value = "{\"id\": 1}",
                            updatedAt = updatedAt,
                        )
                    ),
                )
                every {
                    draftDesignService.getMyDraftDesigns(any())
                } returns Flux.fromIterable(draftDesigns)

                val response = exchangeRequest()
                    .expectBody<TestResponse<List<MyDraftDesigns.Response>>>()
                    .returnResult()

                it("service 를 통해 생성 요청해야 함") {
                    verify(exactly = 1) {
                        draftDesignService.getMyDraftDesigns(WebTestClientHelper.AUTHORIZED_KNITTER_ID)
                    }
                }
                it("작성중인 도안 리스트가 반환되어야 함") {
                    response.status.is2xxSuccessful shouldBe true
                    response.responseBody?.payload shouldBe listOf(
                        MyDraftDesigns.Response(
                            id = 1,
                            name = "작성 중",
                            updatedAt = updatedAt,
                        ),
                        MyDraftDesigns.Response(
                            id = 2,
                            name = null,
                            updatedAt = updatedAt,
                        )
                    )
                }
            }

            context("작성 중인 도안이 없는 경우") {
                every {
                    draftDesignService.getMyDraftDesigns(any())
                } returns Flux.empty()

                val response = exchangeRequest()
                    .expectBody<TestResponse<List<MyDraftDesigns.Response>>>()
                    .returnResult()

                it("service 를 통해 생성 요청해야 함") {
                    verify(exactly = 1) {
                        draftDesignService.getMyDraftDesigns(WebTestClientHelper.AUTHORIZED_KNITTER_ID)
                    }
                }
                it("빈 리스트가 반환되어야 함") {
                    response.status.is2xxSuccessful shouldBe true
                    response.responseBody?.payload shouldBe emptyList()
                }
            }
        }

        describe("내 작성중인 도안 조회 test") {
            val exchangeRequest = fun (): WebTestClient.ResponseSpec =
                webClient
                    .get()
                    .uri("/designs/draft/mine/1")
                    .addDefaultRequestHeader()
                    .exchange()

            context("작성 중인 도안이 있는 경우") {
                val updatedAt = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC)
                val draftDesign = MockFactory.create(
                    MockData.DraftDesign(
                        id = 1,
                        value = "{\"name\": \"작성 중\"}",
                        updatedAt = updatedAt,
                    )
                )
                every {
                    draftDesignService.getMyDraftDesign(any(), any())
                } returns Mono.just(draftDesign)

                val response = exchangeRequest()
                    .expectBody<TestResponse<MyDraftDesign.Response>>()
                    .returnResult()

                it("service 를 통해 생성 요청해야 함") {
                    verify(exactly = 1) {
                        draftDesignService
                            .getMyDraftDesign(1, WebTestClientHelper.AUTHORIZED_KNITTER_ID)
                    }
                }
                it("작성중인 도안이 반환되어야 함") {
                    val expectedResponse = MyDraftDesign.Response(
                        id = 1,
                        value = "{\"name\": \"작성 중\"}",
                        updatedAt = updatedAt,
                    )
                    response.status.is2xxSuccessful shouldBe true
                    response.responseBody?.payload shouldBe expectedResponse
                }
            }

            context("작성 중인 도안이 없는 경우") {
                every {
                    draftDesignService.getMyDraftDesign(any(), any())
                } returns Mono.error(NotFoundEntity(DraftDesign::class.java))

                val response = exchangeRequest()
                    .expectBody<TestResponse<MyDraftDesign.Response>>()
                    .returnResult()

                it("service 를 통해 생성 요청해야 함") {
                    verify(exactly = 1) {
                        draftDesignService.getMyDraftDesign(1, WebTestClientHelper.AUTHORIZED_KNITTER_ID)
                    }
                }
                it("NOT FOUND 에러가 발생되어야 함") {
                    response.rawStatusCode shouldBe 404
                }
            }
        }
    }
}
