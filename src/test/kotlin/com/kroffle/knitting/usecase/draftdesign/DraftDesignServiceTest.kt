package com.kroffle.knitting.usecase.draftdesign

import com.kroffle.knitting.domain.draftdesign.entity.DraftDesign
import com.kroffle.knitting.helper.MockData
import com.kroffle.knitting.helper.MockFactory
import com.kroffle.knitting.infra.persistence.exception.NotFoundEntity
import com.kroffle.knitting.usecase.draftdesign.dto.SaveDraftDesignData
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.test.expectError
import reactor.kotlin.test.test

class DraftDesignServiceTest : BehaviorSpec() {
    init {
        val mockDraftDesignRepository = mockk<DraftDesignService.DraftDesignRepository>()
        val mockDesignRepository = mockk<DraftDesignService.DesignRepository>()
        val service = DraftDesignService(mockDraftDesignRepository, mockDesignRepository)

        afterContainer { clearAllMocks() }

        Given("내가 작성 중이던 도안이 존재하고") {
            val mockDraftDesign = MockFactory.create(MockData.DraftDesign(id = 1))

            When("작성 중이던 도안리스트를 조회하면") {
                every {
                    mockDraftDesignRepository.findNewDraftDesignsByKnitterId(any())
                } returns Flux.fromIterable(listOf(mockDraftDesign))

                val result = service.getMyDraftDesigns(1).collectList().block()
                Then("작성 중이던 도안 목록을 조회해와야 한다") {
                    verify(exactly = 1) {
                        mockDraftDesignRepository
                            .findNewDraftDesignsByKnitterId(1)
                    }
                }
                Then("작성 중이던 도안 리스트가 반환되어야 한다") {
                    result shouldBe listOf(mockDraftDesign)
                }
            }

            When("작성 중이던 도안 상세를 조회하면") {
                every {
                    mockDraftDesignRepository.findByIdAndKnitterId(any(), any())
                } returns Mono.just(mockDraftDesign)

                val result = service.getMyDraftDesign(1, 1).block()
                Then("작성 중이던 도안을 조회해와야 한다") {
                    verify(exactly = 1) {
                        mockDraftDesignRepository.findByIdAndKnitterId(1, 1)
                    }
                }
                Then("작성 중이던 도안 상세가 반환되어야 한다") {
                    result shouldBe mockDraftDesign
                }
            }

            When("작성 중이던 도안을 삭제하면") {
                every {
                    mockDraftDesignRepository
                        .findByIdAndKnitterId(any(), any())
                } returns Mono.just(mockDraftDesign)

                every {
                    mockDraftDesignRepository.delete(any())
                } returns Mono.just(1)

                val result = service.deleteMyDraftDesign(1, 1).block()

                Then("작성 중이던 도안을 조회해야 한다") {
                    verify(exactly = 1) {
                        mockDraftDesignRepository.findByIdAndKnitterId(1, 1)
                    }
                }
                Then("작성 중이던 도안을 삭제해야 한다") {
                    verify(exactly = 1) {
                        mockDraftDesignRepository.delete(mockDraftDesign)
                    }
                }
                Then("삭제된 도안 id가 반환되어야 한다") {
                    result shouldBe mockDraftDesign.id
                }
            }
        }

        Given("내가 작성 중이던 도안이 존재하지 않고") {
            When("작성 중이던 도안리스트를 조회하면") {
                every {
                    mockDraftDesignRepository.findNewDraftDesignsByKnitterId(any())
                } returns Flux.empty()

                val result = service.getMyDraftDesigns(1).collectList().block()
                Then("작성 중이던 도안 목록을 조회해와야 한다") {
                    verify(exactly = 1) {
                        mockDraftDesignRepository.findNewDraftDesignsByKnitterId(1)
                    }
                }
                Then("빈 리스트가 반환되어야 한다") {
                    result shouldBe emptyList()
                }
            }

            When("작성 중이던 도안 상세를 조회하면") {
                every {
                    mockDraftDesignRepository.findByIdAndKnitterId(any(), any())
                } returns Mono.error(NotFoundEntity(DraftDesign::class.java))

                val result = service.deleteMyDraftDesign(1, 1).test()

                Then("작성 중이던 도안을 조회해와야 한다") {
                    verify(exactly = 1) {
                        mockDraftDesignRepository.findByIdAndKnitterId(1, 1)
                    }
                }
                Then("NotFoundEntity exception 이 발생해야 한다") {
                    result.expectError(NotFoundEntity::class).verify()
                }
            }

            When("작성 중이던 도안을 삭제하면") {
                every {
                    mockDraftDesignRepository.findByIdAndKnitterId(any(), any())
                } returns Mono.error(NotFoundEntity(DraftDesign::class.java))

                val result = service.deleteMyDraftDesign(1, 1).test()

                Then("작성 중이던 도안을 조회해와야 한다") {
                    verify(exactly = 1) {
                        mockDraftDesignRepository.findByIdAndKnitterId(1, 1)
                    }
                }

                Then("작성 중이던 도안을 삭제요청 하지 않는다") {
                    verify(inverse = true) {
                        mockDraftDesignRepository.delete(any())
                    }
                }

                Then("NotFoundEntity exception 이 발생해야 한다") {
                    result.expectError(NotFoundEntity::class).verify()
                }
            }
        }

        Given("새로운 도안을 생성 중인 임시저장 내역이 주어지고") {
            val designId = null
            val mockDraftDesign = MockFactory.create(
                MockData.DraftDesign(
                    id = 1,
                    value = "\"name\": \"도안 이름\""
                )
            )
            When("기존에 저장된 이력이 없는 경우") {
                val draftDesignId = null
                val data = SaveDraftDesignData(
                    id = draftDesignId,
                    knitterId = 1,
                    designId = designId,
                    value = "\"name\": \"도안 이름\"",
                )
                val saveArgument = slot<DraftDesign>()
                every {
                    mockDraftDesignRepository.save(capture(saveArgument))
                } returns Mono.just(mockDraftDesign)

                val result = service.saveDraft(data).block()
                Then("새로운 임시저장 내역을 생성해야 한다") {
                    saveArgument.isCaptured shouldBe true
                    with(saveArgument.captured) {
                        id shouldBe null
                        knitterId shouldBe 1
                        designId shouldBe null
                        value shouldBe "\"name\": \"도안 이름\""
                    }
                }
                Then("생성된 임시저장 내역을 반환해야 한다") {
                    result shouldBe mockDraftDesign
                }
            }

            When("기존에 저장된 이력이 있는 경우") {
                val draftDesignId = 1L
                val beforeDraftDesign = MockFactory.create(
                    MockData.DraftDesign(id = draftDesignId)
                )
                val data = SaveDraftDesignData(
                    id = draftDesignId,
                    knitterId = 1,
                    designId = designId,
                    value = "\"name\": \"도안 이름\"",
                )
                val saveArgument = slot<DraftDesign>()

                every {
                    mockDraftDesignRepository
                        .findByIdAndKnitterId(any(), any())
                } returns Mono.just(beforeDraftDesign)

                every {
                    mockDraftDesignRepository.save(capture(saveArgument))
                } returns Mono.just(mockDraftDesign)

                val result = service.saveDraft(data).block()
                Then("기존 임시저장 내역을 조회해야 한다") {
                    verify(exactly = 1) {
                        mockDraftDesignRepository.findByIdAndKnitterId(1, 1)
                    }
                }
                Then("기존 임시저장 내역을 수정하여 저장해야 한다") {
                    saveArgument.isCaptured shouldBe true
                    with(saveArgument.captured) {
                        id shouldBe 1
                        knitterId shouldBe 1
                        designId shouldBe null
                        value shouldBe "\"name\": \"도안 이름\""
                    }
                }
                Then("생성된 임시저장 내역을 반환해야 한다") {
                    result shouldBe mockDraftDesign
                }
            }
        }

        Given("기존 도안을 수정 중인 임시저장 내역이 주어지고") {
            val designId = 1L
            val mockDesign = MockFactory.create(MockData.Design(id = 1))
            val mockDraftDesign = MockFactory.create(
                MockData.DraftDesign(
                    id = 1,
                    value = "\"name\": \"도안 이름\""
                )
            )
            When("기존에 저장된 이력이 없는 경우") {
                val draftDesignId = null
                val data = SaveDraftDesignData(
                    id = draftDesignId,
                    knitterId = 1,
                    designId = designId,
                    value = "\"name\": \"도안 이름\"",
                )
                val saveArgument = slot<DraftDesign>()

                every {
                    mockDesignRepository.findByIdAndKnitterId(any(), any())
                } returns Mono.just(mockDesign)

                every {
                    mockDraftDesignRepository.save(capture(saveArgument))
                } returns Mono.just(mockDraftDesign)

                val result = service.saveDraft(data).block()
                Then("존재하는 도안인지 검증해야 한다") {
                    verify(exactly = 1) {
                        mockDesignRepository.findByIdAndKnitterId(1, 1)
                    }
                }
                Then("새로운 임시저장 내역을 생성해야 한다") {
                    saveArgument.isCaptured shouldBe true
                    with(saveArgument.captured) {
                        id shouldBe null
                        knitterId shouldBe 1
                        designId shouldBe 1
                        value shouldBe "\"name\": \"도안 이름\""
                    }
                }
                Then("생성된 임시저장 내역을 반환해야 한다") {
                    result shouldBe mockDraftDesign
                }
            }

            When("기존에 저장된 이력이 있는 경우") {
                val draftDesignId = 1L
                val beforeDraftDesign = MockFactory.create(
                    MockData.DraftDesign(id = draftDesignId)
                )
                val data = SaveDraftDesignData(
                    id = draftDesignId,
                    knitterId = 1,
                    designId = designId,
                    value = "\"name\": \"도안 이름\"",
                )
                val saveArgument = slot<DraftDesign>()

                every {
                    mockDesignRepository.findByIdAndKnitterId(any(), any())
                } returns Mono.just(mockDesign)

                every {
                    mockDraftDesignRepository
                        .findByIdAndKnitterId(any(), any())
                } returns Mono.just(beforeDraftDesign)

                every {
                    mockDraftDesignRepository.save(capture(saveArgument))
                } returns Mono.just(mockDraftDesign)

                val result = service.saveDraft(data).block()
                Then("존재하는 도안인지 검증해야 한다") {
                    verify(exactly = 1) {
                        mockDesignRepository.findByIdAndKnitterId(1, 1)
                    }
                }
                Then("기존 임시저장 내역을 조회해야 한다") {
                    verify(exactly = 1) {
                        mockDraftDesignRepository.findByIdAndKnitterId(1, 1)
                    }
                }
                Then("기존 임시저장 내역을 수정하여 저장해야 한다") {
                    saveArgument.isCaptured shouldBe true
                    with(saveArgument.captured) {
                        id shouldBe 1
                        knitterId shouldBe 1
                        designId shouldBe 1
                        value shouldBe "\"name\": \"도안 이름\""
                    }
                }
                Then("생성된 임시저장 내역을 반환해야 한다") {
                    result shouldBe mockDraftDesign
                }
            }
        }
    }
}
