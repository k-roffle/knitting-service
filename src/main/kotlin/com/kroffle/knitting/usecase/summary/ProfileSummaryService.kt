package com.kroffle.knitting.usecase.summary

import com.kroffle.knitting.usecase.summary.dto.ProfileSummary
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ProfileSummaryService(
    private val productRepository: ProductRepository,
    private val designRepository: DesignRepository,
) {
    fun getProfileSummary(knitterId: Long): Mono<ProfileSummary> =
        Mono
            .zip(
                productRepository.countMyProducts(knitterId),
                productRepository.countPurchasedProducts(knitterId),
                designRepository.countMyDesigns(knitterId)
            )
            .map {
                ProfileSummary(
                    myProductsCount = it.t1,
                    purchasedProductsCount = it.t2,
                    myDesignsCount = it.t3,
                )
            }

    interface ProductRepository {
        fun countMyProducts(knitterId: Long): Mono<Int>
        fun countPurchasedProducts(knitterId: Long): Mono<Int>
    }

    interface DesignRepository {
        fun countMyDesigns(knitterId: Long): Mono<Int>
    }
}
