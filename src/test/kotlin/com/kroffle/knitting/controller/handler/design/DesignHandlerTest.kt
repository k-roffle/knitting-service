package com.kroffle.knitting.controller.handler.design

import com.kroffle.knitting.common.extensions.onUTC
import com.kroffle.knitting.controller.handler.design.dto.MyDesign
import com.kroffle.knitting.controller.handler.design.dto.MyDesigns
import com.kroffle.knitting.controller.handler.design.dto.NewDesign
import com.kroffle.knitting.controller.handler.design.dto.SizeDto
import com.kroffle.knitting.controller.handler.design.dto.UpdateDesign
import com.kroffle.knitting.controller.handler.exception.EmptyBodyException
import com.kroffle.knitting.controller.handler.exception.InvalidBodyException
import com.kroffle.knitting.controller.router.design.DesignsRouter
import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.design.value.Gauge
import com.kroffle.knitting.domain.design.value.Length
import com.kroffle.knitting.domain.design.value.Pattern
import com.kroffle.knitting.domain.design.value.Size
import com.kroffle.knitting.domain.design.value.Technique
import com.kroffle.knitting.domain.draftdesign.entity.DraftDesign
import com.kroffle.knitting.domain.value.Money
import com.kroffle.knitting.helper.MockData
import com.kroffle.knitting.helper.MockFactory
import com.kroffle.knitting.helper.TestResponse
import com.kroffle.knitting.helper.WebTestClientHelper
import com.kroffle.knitting.helper.extension.addDefaultRequestHeader
import com.kroffle.knitting.infra.persistence.exception.NotFoundEntity
import com.kroffle.knitting.usecase.design.DesignService
import com.kroffle.knitting.usecase.design.dto.CreateDesignData
import com.kroffle.knitting.usecase.design.dto.GetMyDesignData
import com.kroffle.knitting.usecase.design.dto.MyDesignFilter
import com.kroffle.knitting.usecase.design.dto.UpdateDesignData
import com.kroffle.knitting.usecase.helper.pagination.type.Paging
import com.kroffle.knitting.usecase.helper.pagination.type.Sort
import com.kroffle.knitting.usecase.helper.pagination.type.SortDirection
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

@DisplayName("DesignHandler Test")
class DesignHandlerTest : DescribeSpec() {
    init {
        val service = mockk<DesignService>()
        val router = DesignsRouter(DesignHandler(service), mockk())
        val webClient = WebTestClientHelper
            .createWebTestClient(router.designsRouterFunction())

        afterContainer {
            clearAllMocks()
        }

        describe("get my design test") {
            val exchangeRequest = fun (designId: Long): WebTestClient.ResponseSpec {
                return webClient
                    .get()
                    .uri("/designs/mine/$designId")
                    .addDefaultRequestHeader()
                    .exchange()
            }

            context("존재하는 도안을 조회하는 경우") {
                every {
                    service.getMyDesign(any())
                } returns Mono.just(MockFactory.create(MockData.Design(id = 1, createdAt = null, updatedAt = null)))

                val response = exchangeRequest(1)
                    .expectBody<TestResponse<MyDesign.Response>>()
                    .returnResult()

                it("service 를 통해 조회해와야 함") {
                    verify(exactly = 1) {
                        service.getMyDesign(
                            GetMyDesignData(
                                1,
                                WebTestClientHelper.AUTHORIZED_KNITTER_ID,
                            )
                        )
                    }
                }

                it("도안 데이터가 반환되어야 함") {
                    response.status.is2xxSuccessful shouldBe true
                    response.responseBody?.payload shouldBe MyDesign.Response(
                        1,
                        name = "도안 이름",
                        designType = Design.DesignType.Sweater,
                        patternType = Design.PatternType.Image,
                        gauge = Gauge(12.5, 13.0),
                        size = SizeDto(
                            sizeUnit = Length.Unit.Cm,
                            totalLength = 65.0,
                            sleeveLength = 73.5,
                            shoulderWidth = 32.0,
                            bottomWidth = 31.0,
                            armholeDepth = 28.5,
                        ),
                        needle = "5.0mm 대바늘",
                        yarn = "캐시미어 100g",
                        extra = null,
                        price = Money.ZERO,
                        pattern = Pattern("스웨터 뜨는 법"),
                        description = "캐시미어 스웨터",
                        targetLevel = Design.LevelType.EASY,
                        coverImageUrl = "https://mock.wordway.com/image.png",
                        techniques = listOf(),
                        updatedAt = null,
                        createdAt = null,
                    )
                }
            }

            context("존재하지 않는 도안을 조회하는 경우") {
                every {
                    service.getMyDesign(any())
                } returns Mono.error(NotFoundEntity(DraftDesign::class.java))

                val response = exchangeRequest(1)
                    .expectBody<TestResponse<MyDesign.Response>>()
                    .returnResult()

                it("service 를 통해 조회해와야 함") {
                    verify(exactly = 1) {
                        service.getMyDesign(
                            GetMyDesignData(
                                1,
                                WebTestClientHelper.AUTHORIZED_KNITTER_ID,
                            )
                        )
                    }
                }

                it("404 가 반환되어야 함") {
                    response.rawStatusCode shouldBe 404
                }
            }
        }

        describe("get my designs test") {
            val exchangeRequest = fun (queryParam: String?): WebTestClient.ResponseSpec {
                return webClient
                    .get()
                    .uri("/designs/mine?$queryParam")
                    .addDefaultRequestHeader()
                    .exchange()
            }
            context("내가 만든 도안이 존재하는 경우") {
                context("첫 번째 페이지를 요청하면") {
                    val now = OffsetDateTime.now().onUTC()
                    val designs = listOf(
                        MockFactory.create(
                            MockData.Design(
                                id = 1,
                                knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
                                createdAt = now,
                            )
                        ),
                    )
                    every {
                        service.getMyDesigns(any())
                    } returns Flux.fromIterable(designs)

                    val response = exchangeRequest(null)
                        .expectBody<TestResponse<List<MyDesigns.Response>>>()
                        .returnResult()

                    it("service 를 통해 조회해와야 함") {
                        verify(exactly = 1) {
                            service.getMyDesigns(
                                MyDesignFilter(
                                    paging = Paging(after = null, count = 10),
                                    knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
                                    sort = Sort("id", SortDirection.DESC),
                                )
                            )
                        }
                    }

                    it("도안 데이터가 반환되어야 함") {
                        response.status.is2xxSuccessful shouldBe true
                        val payload = response.responseBody?.payload!!
                        payload.size shouldBe 1
                        payload.first() shouldBe MyDesigns.Response(
                            id = 1,
                            name = "도안 이름",
                            yarn = "캐시미어 100g",
                            coverImageUrl = "https://mock.wordway.com/image.png",
                            tags = listOf("니트", "이미지도안"),
                            createdAt = now,
                        )
                    }
                }

                context("더 불러오기 요청을 하면") {
                    val designs = listOf(MockFactory.create(MockData.Design(id = 1)))
                    every {
                        service.getMyDesigns(any())
                    } returns Flux.fromIterable(designs)

                    exchangeRequest("after=2&count=20")
                        .expectBody<TestResponse<List<MyDesigns.Response>>>()
                        .returnResult()

                    it("페이지 정보가 적절히 넘어가야 함") {
                        verify(exactly = 1) {
                            service.getMyDesigns(
                                MyDesignFilter(
                                    paging = Paging(after = "2", count = 20),
                                    knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
                                    sort = Sort("id", SortDirection.DESC),
                                )
                            )
                        }
                    }
                }
            }

            context("내가 만든 도안이 존재하지 않는 경우") {
                every {
                    service.getMyDesigns(any())
                } returns Flux.empty()

                val response = exchangeRequest(null)
                    .expectBody<TestResponse<List<MyDesigns.Response>>>()
                    .returnResult()

                it("service 를 통해 조회해와야 함") {
                    verify(exactly = 1) {
                        service.getMyDesigns(
                            MyDesignFilter(
                                paging = Paging(after = null, count = 10),
                                knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
                                sort = Sort("id", SortDirection.DESC),
                            )
                        )
                    }
                }

                it("빈 리스트가 반환되어야 함") {
                    response.status.is2xxSuccessful shouldBe true
                    response.responseBody?.payload?.size shouldBe 0
                }
            }
        }

        describe("create design test") {
            val exchangeRequest = fun (requestBody: String?): WebTestClient.ResponseSpec {
                val client = webClient
                    .post()
                    .uri("/designs")
                    .addDefaultRequestHeader()
                return if (requestBody == null) {
                    client
                } else {
                    client.bodyValue(requestBody)
                }.exchange()
            }

            context("정상적인 도안 생성을 요청한 경우") {
                every {
                    service.create(any())
                } returns Mono.just(MockFactory.create(MockData.Design(id = 1)))
                val requestBody = """{
                    "name": "도안 이름",
                    "design_type": "Sweater",
                    "pattern_type": "Text",
                    "stitches": 15.0,
                    "rows": 18.5,
                    "size": {
                        "total_length": 58.0,
                        "sleeve_length": 65.3,
                        "shoulder_width": 33.5,
                        "bottom_width": 35.1,
                        "armhole_depth": 22.2
                    },
                    "needle": "5.0mm 둘레바늘 80cm",
                    "yarn": "캐시미어 블랙 400g",
                    "extra": "extra",
                    "price": 1000,
                    "description": "캐시미어로 만드는 뽀송뽀송 탑다운 니트",
                    "target_level": "NORMAL",
                    "cover_image_url": "https://mock.wordway.com/test.png",
                    "pattern": "우아아앙",
                    "techniques": ["안뜨기", "겉뜨기"],
                    "draft_id": null
                }""".trimMargin()

                val response = exchangeRequest(requestBody)
                    .expectBody<TestResponse<NewDesign.Response>>()
                    .returnResult()

                it("service 를 통해 생성 요청해야 함") {
                    verify(exactly = 1) {
                        service.create(
                            CreateDesignData(
                                knitterId = 1,
                                name = "도안 이름",
                                designType = Design.DesignType.Sweater,
                                patternType = Design.PatternType.Text,
                                gauge = Gauge(15.0, 18.5),
                                size = Size(
                                    totalLength = Length(58.0),
                                    sleeveLength = Length(65.3),
                                    shoulderWidth = Length(33.5),
                                    bottomWidth = Length(35.1),
                                    armholeDepth = Length(22.2),
                                ),
                                needle = "5.0mm 둘레바늘 80cm",
                                yarn = "캐시미어 블랙 400g",
                                extra = "extra",
                                price = Money(1000),
                                description = "캐시미어로 만드는 뽀송뽀송 탑다운 니트",
                                targetLevel = Design.LevelType.NORMAL,
                                coverImageUrl = "https://mock.wordway.com/test.png",
                                pattern = Pattern("우아아앙"),
                                techniques = listOf(Technique("안뜨기"), Technique("겉뜨기")),
                                draftId = null,
                            )
                        )
                    }
                }
                it("생성된 도안 id가 반환되어야 함") {
                    response.status.is2xxSuccessful shouldBe true
                    response.responseBody?.payload shouldBe NewDesign.Response(1)
                }
            }

            context("request body 에 필드가 부족한 경우") {
                val requestBody = """{
                    "name": "도안 이름"
                }""".trimMargin()

                val response = exchangeRequest(requestBody)
                    .expectBody<InvalidBodyException>()
                    .returnResult()

                it("400 에러가 발생해야 함") {
                    response.rawStatusCode shouldBe 400
                }
            }

            context("request body 가 없는 경우") {
                val response = exchangeRequest(null)
                    .expectBody<EmptyBodyException>()
                    .returnResult()

                it("400 에러가 발생해야 함") {
                    response.rawStatusCode shouldBe 400
                }
            }
        }

        describe("update design test") {
            val exchangeRequest = fun (designId: Long, requestBody: String?): WebTestClient.ResponseSpec {
                val client = webClient
                    .put()
                    .uri("/designs/$designId")
                    .addDefaultRequestHeader()
                return if (requestBody == null) {
                    client
                } else {
                    client.bodyValue(requestBody)
                }.exchange()
            }

            context("정상적인 도안 생성을 요청한 경우") {
                val designId: Long = 1
                every {
                    service.update(any())
                } returns Mono.just(MockFactory.create(MockData.Design(id = designId)))
                val requestBody = """{
                    "design_type": "Sweater",
                    "pattern_type": "Text",
                    "stitches": 17.0,
                    "rows": 20.5,
                    "size": {
                        "total_length": 10.0,
                        "sleeve_length": 20.0,
                        "shoulder_width": 30.0,
                        "bottom_width": 40.0,
                        "armhole_depth": 50.0
                    },
                    "needle": "5.0mm 둘레바늘 80cm",
                    "yarn": "캐시미어 블랙 400g + 200g (수정)",
                    "extra": "extra",
                    "description": "캐시미어로 만드는 뽀송뽀송 탑다운 니트",
                    "target_level": "NORMAL",
                    "pattern": "우아아앙",
                    "techniques": ["겉뜨기"],
                    "draft_id": null
                }""".trimMargin()

                val response = exchangeRequest(designId, requestBody)
                    .expectBody<TestResponse<UpdateDesign.Response>>()
                    .returnResult()

                it("service 를 통해 생성 요청해야 함") {
                    verify(exactly = 1) {
                        service.update(
                            UpdateDesignData(
                                id = designId,
                                knitterId = 1,
                                designType = Design.DesignType.Sweater,
                                patternType = Design.PatternType.Text,
                                gauge = Gauge(17.0, 20.5),
                                size = Size(
                                    totalLength = Length(10.0),
                                    sleeveLength = Length(20.0),
                                    shoulderWidth = Length(30.0),
                                    bottomWidth = Length(40.0),
                                    armholeDepth = Length(50.0),
                                ),
                                needle = "5.0mm 둘레바늘 80cm",
                                yarn = "캐시미어 블랙 400g + 200g (수정)",
                                extra = "extra",
                                description = "캐시미어로 만드는 뽀송뽀송 탑다운 니트",
                                targetLevel = Design.LevelType.NORMAL,
                                pattern = Pattern("우아아앙"),
                                techniques = listOf(Technique("겉뜨기")),
                                draftId = null,
                            )
                        )
                    }
                }
                it("생성된 도안 id가 반환되어야 함") {
                    response.status.is2xxSuccessful shouldBe true
                    response.responseBody?.payload shouldBe UpdateDesign.Response(designId)
                }
            }

            context("request body 에 필드가 부족한 경우") {
                val requestBody = """{
                    "name": "도안 이름"
                }""".trimMargin()

                val response = exchangeRequest(1, requestBody)
                    .expectBody<InvalidBodyException>()
                    .returnResult()

                it("400 에러가 발생해야 함") {
                    response.rawStatusCode shouldBe 400
                }
            }

            context("request body 가 없는 경우") {
                val response = exchangeRequest(1, null)
                    .expectBody<EmptyBodyException>()
                    .returnResult()

                it("400 에러가 발생해야 함") {
                    response.rawStatusCode shouldBe 400
                }
            }
        }
    }
}
