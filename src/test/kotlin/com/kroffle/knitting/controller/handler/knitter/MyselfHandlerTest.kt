package com.kroffle.knitting.controller.handler.knitter

import com.kroffle.knitting.controller.handler.knitter.dto.MyProfile
import com.kroffle.knitting.controller.router.knitter.MyselfRouter
import com.kroffle.knitting.helper.MockData
import com.kroffle.knitting.helper.MockFactory
import com.kroffle.knitting.helper.TestResponse
import com.kroffle.knitting.helper.WebTestClientHelper
import com.kroffle.knitting.helper.extension.addDefaultRequestHeader
import com.kroffle.knitting.usecase.knitter.KnitterService
import com.kroffle.knitting.usecase.summary.ProfileSummaryService
import io.kotest.core.spec.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Mono

@DisplayName("MyselfHandler Test")
class MyselfHandlerTest : DescribeSpec() {
    init {
        val knitterService = mockk<KnitterService>()
        val profileSummaryService = mockk<ProfileSummaryService>()
        val myselfRouter = MyselfRouter(MyselfHandler(knitterService, mockk(), profileSummaryService))
        val myselfWebClient = WebTestClientHelper
            .createWebTestClient(myselfRouter.profileRouterFunction())

        afterContainer { clearAllMocks() }
        describe("getMyProfile test") {
            val knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID
            val mockKnitter = MockFactory.create(MockData.Knitter(id = knitterId))
            context("로그인 후 프로필을 요청한 경우") {
                every {
                    knitterService.getKnitter(any())
                } returns Mono.just(mockKnitter)

                val response = myselfWebClient
                    .get()
                    .uri("/me/profile")
                    .addDefaultRequestHeader()
                    .exchange()
                    .expectBody<TestResponse<MyProfile.Response>>()
                    .returnResult()

                it("service 를 통해 조회 요청해야 함") {
                    verify(exactly = 1) {
                        knitterService.getKnitter(knitterId)
                    }
                }
                it("프로필 정보가 반환되어야 함") {
                    response.status.is2xxSuccessful shouldBe true
                    response.responseBody?.payload shouldBe MyProfile.Response(
                        email = mockKnitter.email,
                        profileImageUrl = mockKnitter.profileImageUrl,
                        name = mockKnitter.name,
                    )
                }
            }

            context("로그인하지 않고 프로필을 요청한 경우") {
                every {
                    knitterService.getKnitter(any())
                } returns Mono.just(mockKnitter)

                val response = myselfWebClient
                    .get()
                    .uri("/me/profile")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectBody<TestResponse<MyProfile.Response>>()
                    .returnResult()

                it("service 를 통해 조회 요청하지 않아야 함") {
                    verify(inverse = true) {
                        knitterService.getKnitter(any())
                    }
                }
                it("404 에러가 반환되어야 함") {
                    response.rawStatusCode shouldBe 400
                }
            }
        }
    }
}
