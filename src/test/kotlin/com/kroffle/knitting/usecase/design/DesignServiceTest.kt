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
import com.kroffle.knitting.infra.persistence.exception.NotFoundEntity
import com.kroffle.knitting.usecase.design.dto.CreateDesignData
import com.kroffle.knitting.usecase.design.dto.UpdateDesignData
import com.kroffle.knitting.usecase.repository.DesignRepository
import com.kroffle.knitting.usecase.repository.DraftDesignRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import reactor.core.publisher.Mono
import reactor.kotlin.test.expectError
import reactor.kotlin.test.test

class DesignServiceTest : BehaviorSpec() {
    init {
        val designRepository = mockk<DesignRepository>()
        val draftDesignRepository = mockk<DraftDesignRepository>()
        val service = DesignService(designRepository, draftDesignRepository)

        afterContainer {
            clearAllMocks()
        }

        Given("저장하고자 하는 도안에 임시저장 내역이 있고") {
            val draftDesign = MockFactory.create(MockData.DraftDesign(id = 1))

            When("도안을 생성하면") {
                val createdDesign = MockFactory.create(MockData.Design(id = 1))
                val createData = createCreateDesignData().copy(draftId = 1)

                every {
                    draftDesignRepository.getDraftDesign(any(), any())
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
                        draftDesignRepository.getDraftDesign(
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
                        designRepository.createDesign(createDesignFromData(createData))
                    }
                }
                Then("생성된 도안을 반환해야 한다") {
                    result shouldBe createdDesign
                }
            }

            When("도안을 수정하면") {
                val designId: Long = 1
                val draftDesignId: Long = 2
                val updateData = createUpdateDesignData(designId).copy(draftId = draftDesignId)
                val mockDesign = MockFactory.create(MockData.Design(id = designId))

                every {
                    draftDesignRepository.getDraftDesign(any(), any())
                } returns Mono.just(draftDesign)

                every {
                    draftDesignRepository.delete(any())
                } returns Mono.just(draftDesignId)

                every {
                    designRepository.updateDesign(any())
                } returns Mono.just(mockDesign)

                every {
                    designRepository.getDesign(any(), any())
                } returns Mono.just(mockDesign)

                val result = service.update(updateData).block()

                Then("유저가 해당 임시저장 내역을 가지고 있는지 확인해야 한다") {
                    verify(exactly = 1) {
                        draftDesignRepository.getDraftDesign(
                            id = draftDesignId,
                            knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
                        )
                    }
                }

                Then("임시저장 내역을 삭제해야 한다") {
                    verify(exactly = 1) {
                        draftDesignRepository.delete(draftDesign)
                    }
                }

                Then("도안 수정을 요청해야 한다") {
                    verify(exactly = 1) {
                        val expectedParam = createDesignFromData(mockDesign, updateData)
                        designRepository.updateDesign(expectedParam)
                    }
                }
                Then("생성된 도안을 반환해야 한다") {
                    result shouldBe mockDesign
                }
            }
        }

        Given("저장하고자 하는 도안에 임시저장 내역이 없고") {
            When("도안을 생성하면") {
                val createData = createCreateDesignData().copy(draftId = null)
                val createdDesign = MockFactory.create(MockData.Design(id = 1))

                every {
                    designRepository.createDesign(any())
                } returns Mono.just(createdDesign)

                val result = service.create(createData).block()

                Then("도안 생성을 요청해야 한다") {
                    verify(exactly = 1) {
                        designRepository.createDesign(createDesignFromData(createData))
                    }
                }
                Then("생성된 도안을 반환해야 한다") {
                    result shouldBe createdDesign
                }
            }

            When("도안을 수정하면") {
                val designId: Long = 1
                val updateData = createUpdateDesignData(designId).copy(draftId = null)
                val mockDesign = MockFactory.create(MockData.Design(id = designId))

                every {
                    designRepository.getDesign(any(), any())
                } returns Mono.just(mockDesign)

                every {
                    designRepository.updateDesign(any())
                } returns Mono.just(mockDesign)

                val result = service.update(updateData).block()

                Then("도안 생성을 요청해야 한다") {
                    verify(exactly = 1) {
                        val expectedParam = createDesignFromData(mockDesign, updateData)
                        designRepository.updateDesign(expectedParam)
                    }
                }
                Then("생성된 도안을 반환해야 한다") {
                    result shouldBe mockDesign
                }
            }
        }
        Given("주어진 도안이 없고") {
            every {
                designRepository.getDesign(any(), any())
            } returns Mono.error(NotFoundEntity(Design::class.java))
            When("도안 수정을 요청할 때") {
                val result = service.update(createUpdateDesignData(1)).test()
                Then("작성 중이던 도안을 조회해와야 한다") {
                    verify(exactly = 1) {
                        designRepository.getDesign(1, 1)
                    }
                }
                Then("NotFoundEntity exception 이 발생해야 한다") {
                    result.expectError(NotFoundEntity::class).verify()
                }
            }
        }
    }

    private fun createCreateDesignData() = CreateDesignData(
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

    private fun createUpdateDesignData(designId: Long) = UpdateDesignData(
        id = designId,
        knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
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
        pattern = Pattern("코를 잡는다."),
        description = "남녀공용 루즈핏 피셔맨니트",
        targetLevel = Design.LevelType.NORMAL,
        techniques = emptyList(),
        draftId = null,
    )

    private fun createDesignFromData(data: CreateDesignData) =
        with(data) {
            Design(
                id = null,
                knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
                name = name,
                designType = designType,
                patternType = patternType,
                gauge = gauge,
                size = size,
                needle = needle,
                yarn = yarn,
                extra = extra,
                price = price,
                pattern = pattern,
                description = description,
                targetLevel = targetLevel,
                coverImageUrl = coverImageUrl,
                techniques = techniques,
                createdAt = null,
            )
        }

    private fun createDesignFromData(design: Design, data: UpdateDesignData) =
        with(data) {
            design.update(
                designType = designType,
                patternType = patternType,
                gauge = gauge,
                size = size,
                needle = needle,
                yarn = yarn,
                extra = extra,
                pattern = pattern,
                description = description,
                targetLevel = targetLevel,
                techniques = techniques,
            )
        }
}
