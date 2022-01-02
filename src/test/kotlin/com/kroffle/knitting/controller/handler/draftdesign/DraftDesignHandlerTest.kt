package com.kroffle.knitting.controller.handler.draftdesign

import com.kroffle.knitting.controller.handler.draftdesign.dto.DeleteDraftDesign
import com.kroffle.knitting.controller.handler.draftdesign.dto.GetMyDraftDesign
import com.kroffle.knitting.controller.handler.draftdesign.dto.GetMyDraftDesignToUpdate
import com.kroffle.knitting.controller.handler.draftdesign.dto.GetMyDraftDesigns
import com.kroffle.knitting.controller.handler.draftdesign.dto.SaveDraftDesign
import com.kroffle.knitting.controller.handler.exception.EmptyBodyException
import com.kroffle.knitting.controller.handler.exception.InvalidBodyException
import com.kroffle.knitting.controller.router.design.DesignsRouter
import com.kroffle.knitting.domain.draftdesign.entity.DraftDesign
import com.kroffle.knitting.helper.MockData
import com.kroffle.knitting.helper.MockFactory
import com.kroffle.knitting.helper.TestResponse
import com.kroffle.knitting.helper.WebTestClientHelper
import com.kroffle.knitting.helper.extension.addDefaultRequestHeader
import com.kroffle.knitting.infra.persistence.exception.NotFoundEntity
import com.kroffle.knitting.usecase.draftdesign.DraftDesignService
import com.kroffle.knitting.usecase.draftdesign.dto.SaveDraftDesignData
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
        val mockDraftDesignService = mockk<DraftDesignService>()
        val designsRouter = DesignsRouter(mockk(), DraftDesignHandler(mockDraftDesignService))
        val designsWebclient = WebTestClientHelper
            .createWebTestClient(designsRouter.designsRouterFunction())

        afterContainer { clearAllMocks() }

        describe("임시저장 저장 test") {
            val exchangeRequest = fun (requestBody: String): WebTestClient.ResponseSpec =
                designsWebclient
                    .post()
                    .uri("/designs/draft")
                    .addDefaultRequestHeader()
                    .bodyValue(requestBody)
                    .exchange()

            context("정상적인 도안 생성을 요청한 경우") {
                val mockDraftDesign = MockFactory.create(MockData.DraftDesign(id = 1))
                every {
                    mockDraftDesignService.saveDraft(any())
                } returns Mono.just(mockDraftDesign)
                val requestBody = """
                {
                    "id": 1,
                    "designId": null,
                    "value": "{\"name\": \"도안이름\"}"
                }
                """.trimIndent()
                val response = exchangeRequest(requestBody)
                    .expectBody<TestResponse<SaveDraftDesign.Response>>()
                    .returnResult()

                it("service 를 통해 생성 요청해야 함") {
                    verify(exactly = 1) {
                        mockDraftDesignService.saveDraft(
                            SaveDraftDesignData(
                                id = 1,
                                knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
                                designId = null,
                                value = "{\"name\": \"도안이름\"}",
                            )
                        )
                    }
                }
                it("생성된 도안 id가 반환되어야 함") {
                    response.status.is2xxSuccessful shouldBe true
                    response.responseBody?.payload shouldBe SaveDraftDesign.Response(id = 1)
                }
            }

            context("request body 에 필드가 부족한 경우") {
                val requestBody = "{\"id\": 1}"
                val response = exchangeRequest(requestBody)
                    .expectBody<InvalidBodyException>()
                    .returnResult()
                it("400 에러가 발생해야 함") {
                    response.rawStatusCode shouldBe 400
                }
            }

            context("request body 가 없는 경우") {
                val response = designsWebclient
                    .post()
                    .uri("/designs/draft")
                    .addDefaultRequestHeader()
                    .exchange()
                    .expectBody<EmptyBodyException>()
                    .returnResult()
                it("400 에러가 발생해야 함") {
                    response.rawStatusCode shouldBe 400
                }
            }
        }

        describe("내 작성중인 도안 리스트 조회 test") {
            val exchangeRequest = fun (): WebTestClient.ResponseSpec =
                designsWebclient
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
                    mockDraftDesignService.getMyDraftDesigns(any())
                } returns Flux.fromIterable(draftDesigns)

                val response = exchangeRequest()
                    .expectBody<TestResponse<List<GetMyDraftDesigns.Response>>>()
                    .returnResult()

                it("service 를 통해 조회 요청해야 함") {
                    verify(exactly = 1) {
                        mockDraftDesignService.getMyDraftDesigns(WebTestClientHelper.AUTHORIZED_KNITTER_ID)
                    }
                }
                it("작성중인 도안 리스트가 반환되어야 함") {
                    response.status.is2xxSuccessful shouldBe true
                    response.responseBody?.payload shouldBe listOf(
                        GetMyDraftDesigns.Response(
                            id = 1,
                            name = "작성 중",
                            updatedAt = updatedAt,
                        ),
                        GetMyDraftDesigns.Response(
                            id = 2,
                            name = null,
                            updatedAt = updatedAt,
                        )
                    )
                }
            }

            context("작성 중인 도안이 없는 경우") {
                every {
                    mockDraftDesignService.getMyDraftDesigns(any())
                } returns Flux.empty()

                val response = exchangeRequest()
                    .expectBody<TestResponse<List<GetMyDraftDesigns.Response>>>()
                    .returnResult()

                it("service 를 통해 조회 요청해야 함") {
                    verify(exactly = 1) {
                        mockDraftDesignService.getMyDraftDesigns(WebTestClientHelper.AUTHORIZED_KNITTER_ID)
                    }
                }
                it("빈 리스트가 반환되어야 함") {
                    response.status.is2xxSuccessful shouldBe true
                    response.responseBody?.payload shouldBe emptyList()
                }
            }
        }

        describe("내 작성중인 도안 상세 조회 test") {
            val exchangeRequest = fun (): WebTestClient.ResponseSpec =
                designsWebclient
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
                    mockDraftDesignService.getMyDraftDesign(any(), any())
                } returns Mono.just(draftDesign)

                val response = exchangeRequest()
                    .expectBody<TestResponse<GetMyDraftDesign.Response>>()
                    .returnResult()

                it("service 를 통해 조회 요청해야 함") {
                    verify(exactly = 1) {
                        mockDraftDesignService
                            .getMyDraftDesign(1, WebTestClientHelper.AUTHORIZED_KNITTER_ID)
                    }
                }
                it("작성중인 도안이 반환되어야 함") {
                    val expectedResponse = GetMyDraftDesign.Response(
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
                    mockDraftDesignService.getMyDraftDesign(any(), any())
                } returns Mono.error(NotFoundEntity(DraftDesign::class.java))

                val response = exchangeRequest()
                    .expectBody<TestResponse<GetMyDraftDesign.Response>>()
                    .returnResult()

                it("service 를 통해 조회 요청해야 함") {
                    verify(exactly = 1) {
                        mockDraftDesignService.getMyDraftDesign(1, WebTestClientHelper.AUTHORIZED_KNITTER_ID)
                    }
                }
                it("NOT FOUND 에러가 발생되어야 함") {
                    response.rawStatusCode shouldBe 404
                }
            }
        }

        describe("내 수정중인 도안 상세 조회 test") {
            val designId = 2L
            val exchangeRequest = fun (): WebTestClient.ResponseSpec =
                designsWebclient
                    .get()
                    .uri("/designs/$designId/draft/mine")
                    .addDefaultRequestHeader()
                    .exchange()

            context("작성 중인 도안이 있는 경우") {
                val updatedAt = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC)
                val draftDesign = MockFactory.create(
                    MockData.DraftDesign(
                        id = 1,
                        designId = designId,
                        value = "{\"name\": \"작성 중\"}",
                        updatedAt = updatedAt,
                    )
                )
                every {
                    mockDraftDesignService.getMyDraftDesignToUpdate(any(), any())
                } returns Mono.just(draftDesign)

                val response = exchangeRequest()
                    .expectBody<TestResponse<GetMyDraftDesignToUpdate.Response>>()
                    .returnResult()

                it("service 를 통해 조회 요청해야 함") {
                    verify(exactly = 1) {
                        mockDraftDesignService
                            .getMyDraftDesignToUpdate(
                                designId = 2,
                                knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
                            )
                    }
                }
                it("수정 중인 도안이 반환되어야 함") {
                    val expectedResponse = GetMyDraftDesignToUpdate.Response(
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
                    mockDraftDesignService.getMyDraftDesignToUpdate(any(), any())
                } returns Mono.error(NotFoundEntity(DraftDesign::class.java))

                val response = exchangeRequest()
                    .expectBody<TestResponse<GetMyDraftDesignToUpdate.Response>>()
                    .returnResult()

                it("service 를 통해 조회 요청해야 함") {
                    verify(exactly = 1) {
                        mockDraftDesignService.getMyDraftDesignToUpdate(
                            designId = 2,
                            knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
                        )
                    }
                }
                it("NOT FOUND 에러가 발생되어야 함") {
                    response.rawStatusCode shouldBe 404
                }
            }
        }

        describe("임시저장 삭제 test") {
            val exchangeRequest = fun (): WebTestClient.ResponseSpec =
                designsWebclient
                    .delete()
                    .uri("/designs/draft/mine/1")
                    .addDefaultRequestHeader()
                    .exchange()

            context("작성 중인 도안이 있는 경우") {
                every {
                    mockDraftDesignService.deleteMyDraftDesign(any(), any())
                } returns Mono.just(1)

                val response = exchangeRequest()
                    .expectBody<TestResponse<DeleteDraftDesign.Response>>()
                    .returnResult()

                it("service 를 통해 삭제 요청해야 함") {
                    verify(exactly = 1) {
                        mockDraftDesignService
                            .deleteMyDraftDesign(1, WebTestClientHelper.AUTHORIZED_KNITTER_ID)
                    }
                }
                it("삭제된 도안 id가 반환되어야 함") {
                    val expectedResponse = DeleteDraftDesign.Response(id = 1)
                    response.status.is2xxSuccessful shouldBe true
                    response.responseBody?.payload shouldBe expectedResponse
                }
            }

            context("작성 중인 도안이 없는 경우") {
                every {
                    mockDraftDesignService.deleteMyDraftDesign(any(), any())
                } returns Mono.error(NotFoundEntity(DraftDesign::class.java))

                val response = exchangeRequest()
                    .expectBody<TestResponse<DeleteDraftDesign.Response>>()
                    .returnResult()

                it("service 를 통해 삭제 요청해야 함") {
                    verify(exactly = 1) {
                        mockDraftDesignService.deleteMyDraftDesign(1, WebTestClientHelper.AUTHORIZED_KNITTER_ID)
                    }
                }
                it("NOT FOUND 에러가 발생되어야 함") {
                    response.rawStatusCode shouldBe 404
                }
            }
        }
    }
}
