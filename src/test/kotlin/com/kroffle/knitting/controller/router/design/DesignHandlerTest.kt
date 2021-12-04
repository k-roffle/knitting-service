package com.kroffle.knitting.controller.router.design

import com.kroffle.knitting.controller.handler.design.DesignHandler
import com.kroffle.knitting.controller.handler.design.dto.NewDesign
import com.kroffle.knitting.controller.handler.exception.EmptyBodyException
import com.kroffle.knitting.controller.handler.exception.InvalidBodyException
import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.design.value.Gauge
import com.kroffle.knitting.domain.design.value.Length
import com.kroffle.knitting.domain.design.value.Pattern
import com.kroffle.knitting.domain.design.value.Size
import com.kroffle.knitting.domain.design.value.Technique
import com.kroffle.knitting.domain.value.Money
import com.kroffle.knitting.helper.MockData
import com.kroffle.knitting.helper.MockFactory
import com.kroffle.knitting.helper.TestResponse
import com.kroffle.knitting.helper.WebTestClientHelper
import com.kroffle.knitting.helper.extension.addDefaultRequestHeader
import com.kroffle.knitting.usecase.design.DesignService
import com.kroffle.knitting.usecase.design.dto.CreateDesignData
import io.kotest.core.spec.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Mono

@DisplayName("DesignRouter Test")
class DesignHandlerTest : DescribeSpec() {
    init {
        val service = mockk<DesignService>()
        val router = DesignRouter(DesignHandler(service), mockk())
        val webClient = WebTestClientHelper
            .createWebTestClient(router.designRouterFunction())

        val exchangeRequest = fun (requestBody: String?): WebTestClient.ResponseSpec {
            val client = webClient
                .post()
                .uri("/design")
                .addDefaultRequestHeader()
            return if (requestBody == null) {
                client
            } else {
                client.bodyValue(requestBody)
            }.exchange()
        }
        describe("create design test") {
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
    }
}
