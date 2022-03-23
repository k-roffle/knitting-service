package com.kroffle.knitting.usecase.summary

import com.kroffle.knitting.helper.WebTestClientHelper
import com.kroffle.knitting.usecase.summary.dto.ProfileSummary
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import reactor.core.publisher.Mono

class ProfileSummaryServiceTest : DescribeSpec() {
    init {
        val productRepository = mockk<ProfileSummaryService.ProductRepository>()
        val designRepository = mockk<ProfileSummaryService.DesignRepository>()
        val service = ProfileSummaryService(productRepository, designRepository)

        describe("getProfileSummary test") {
            val knitterId = WebTestClientHelper.AUTHORIZED_KNITTER_ID
            context("profile summary를 조회하는 경우") {
                every {
                    productRepository.countMyProducts(any())
                } returns Mono.just(1)
                every {
                    productRepository.countPurchasedProducts(any())
                } returns Mono.just(2)
                every {
                    designRepository.countMyDesigns(any())
                } returns Mono.just(3)

                val result = service.getProfileSummary(knitterId).block()

                it("knitterId를 통해 데이터를 조회해와야 함") {
                    verify(exactly = 1) {
                        productRepository.countMyProducts(knitterId)
                    }
                    verify(exactly = 1) {
                        productRepository.countPurchasedProducts(knitterId)
                    }
                    verify(exactly = 1) {
                        designRepository.countMyDesigns(knitterId)
                    }
                }

                it("summary 데이터가 반환되어야 함") {
                    result shouldBe ProfileSummary(
                        myProductsCount = 1,
                        purchasedProductsCount = 2,
                        myDesignsCount = 3,
                    )
                }
            }
        }
    }
}
