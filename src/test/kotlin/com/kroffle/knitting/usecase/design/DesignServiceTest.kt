package com.kroffle.knitting.usecase.design

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.design.value.Gauge
import com.kroffle.knitting.domain.design.value.Length
import com.kroffle.knitting.domain.design.value.Pattern
import com.kroffle.knitting.domain.design.value.Size
import com.kroffle.knitting.domain.value.Money
import com.kroffle.knitting.helper.MockData
import com.kroffle.knitting.helper.MockFactory
import com.kroffle.knitting.helper.WebTestClientHelper
import com.kroffle.knitting.usecase.design.dto.CreateDesignData
import com.kroffle.knitting.usecase.repository.DesignRepository
import com.kroffle.knitting.usecase.repository.DraftDesignRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import reactor.core.publisher.Mono

class DesignServiceTest : BehaviorSpec() {
    init {
        val designRepository = mockk<DesignRepository>()
        val draftDesignRepository = mockk<DraftDesignRepository>()
        val service = DesignService(designRepository, draftDesignRepository)

        afterContainer {
            clearAllMocks()
        }

        val baseCreateData = CreateDesignData(
            knitterId = 1,
            name = "도안 이름",
            designType = Design.DesignType.Sweater,
            patternType = Design.PatternType.Text,
            gauge = Gauge(14.0, 15.5),
            size = Size(
                totalLength = Length(10.0),
                sleeveLength = Length(10.0),
                shoulderWidth = Length(10.0),
                bottomWidth = Length(10.0),
                armholeDepth = Length(10.0),
            ),
            needle = "둘레바늘 5.0mm 80cm",
            yarn = "메리노울 캐시미어 합사",
            extra = null,
            price = Money.ZERO,
            pattern = Pattern("코를 잡는다."),
            description = "남녀공용 루즈핏 피셔맨니트",
            targetLevel = Design.LevelType.NORMAL,
            coverImageUrl = "http://mock.wordway.com/image.png",
            techniques = emptyList(),
            draftId = null,
        )
        val requestedDesign = Design(
            id = null,
            knitterId = 1,
            name = "도안 이름",
            designType = Design.DesignType.Sweater,
            patternType = Design.PatternType.Text,
            gauge = Gauge(14.0, 15.5),
            size = Size(
                totalLength = Length(10.0),
                sleeveLength = Length(10.0),
                shoulderWidth = Length(10.0),
                bottomWidth = Length(10.0),
                armholeDepth = Length(10.0),
            ),
            needle = "둘레바늘 5.0mm 80cm",
            yarn = "메리노울 캐시미어 합사",
            extra = null,
            price = Money.ZERO,
            pattern = Pattern("코를 잡는다."),
            description = "남녀공용 루즈핏 피셔맨니트",
            targetLevel = Design.LevelType.NORMAL,
            coverImageUrl = "http://mock.wordway.com/image.png",
            techniques = emptyList(),
            createdAt = null,
        )
        val createdDesign = MockFactory.create(MockData.Design(id = 1))

        Given("저장하고자 하는 도안에") {

            When("임시저장 내역이 있을 때") {
                val createData = baseCreateData.copy(draftId = 1)
                val draftDesign = MockFactory.create(MockData.DraftDesign(id = 1))

                every {
                    draftDesignRepository.findByIdAndKnitterId(any(), any())
                } returns Mono.just(draftDesign)

                every {
                    draftDesignRepository.delete(any())
                } returns Mono.just(1)

                every {
                    designRepository.createDesign(any())
                } returns Mono.just(createdDesign)

                val result = service.create(createData).block()

                Then("유저가 해당 임시저장 내역을 가지고 있는지 확인해야 한다") {
                    verify(exactly = 1) {
                        draftDesignRepository.findByIdAndKnitterId(
                            id = 1,
                            knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
                        )
                    }
                }

                Then("임시저장 내역을 삭제해야 한다") {
                    verify(exactly = 1) {
                        draftDesignRepository.delete(draftDesign)
                    }
                }

                Then("도안 생성을 요청해야 한다") {
                    verify(exactly = 1) {
                        designRepository.createDesign(requestedDesign)
                    }
                }
                Then("생성된 도안을 반환해야 한다") {
                    result shouldBe createdDesign
                }
            }
            When("임시저장 내역이 없을 때") {
                val createData = baseCreateData.copy(draftId = null)
                every {
                    designRepository.createDesign(any())
                } returns Mono.just(createdDesign)

                val result = service.create(createData).block()

                Then("도안 생성을 요청해야 한다") {
                    verify(exactly = 1) {
                        designRepository.createDesign(requestedDesign)
                    }
                }
                Then("생성된 도안을 반환해야 한다") {
                    result shouldBe createdDesign
                }
            }
        }
    }
}
