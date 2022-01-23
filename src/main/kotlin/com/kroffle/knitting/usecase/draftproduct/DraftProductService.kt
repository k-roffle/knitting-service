package com.kroffle.knitting.usecase.draftproduct

import com.kroffle.knitting.domain.draftproduct.entity.DraftProduct
import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.usecase.draftproduct.dto.SaveDraftProductData
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class DraftProductService(
    private val draftProductRepository: DraftProductRepository,
    private val productRepository: ProductRepository,
) {
    private fun verifyProductId(productId: Long, knitterId: Long): Mono<Product> {
        return productRepository.getProduct(productId, knitterId)
    }

    private fun saveDraftProduct(data: SaveDraftProductData): Mono<DraftProduct> {
        return if (data.id == null) {
            draftProductRepository.create(
                DraftProduct.new(
                    knitterId = data.knitterId,
                    productId = data.productId,
                    value = data.value,
                )
            )
        } else {
            draftProductRepository
                .getDraftProduct(data.id, data.knitterId)
                .map { it.merge(data.value) }
                .flatMap {
                    draftProductRepository.update(it)
                }
        }
    }

    fun saveDraft(data: SaveDraftProductData): Mono<DraftProduct> {
        return if (data.productId == null) {
            saveDraftProduct(data)
        } else {
            verifyProductId(data.productId, data.knitterId)
                .flatMap { saveDraftProduct(data) }
        }
    }

    fun getMyDraftProducts(knitterId: Long): Flux<DraftProduct> =
        draftProductRepository.findDraftProductsToCreate(knitterId)

    fun getMyDraftProduct(draftProductId: Long, knitterId: Long): Mono<DraftProduct> =
        draftProductRepository.getDraftProduct(
            id = draftProductId,
            knitterId = knitterId,
        )

    fun getMyDraftProductToUpdate(productId: Long, knitterId: Long): Mono<DraftProduct> =
        draftProductRepository.getDraftProductToUpdate(
            productId = productId,
            knitterId = knitterId,
        )

    fun deleteMyDraftProduct(draftProductId: Long, knitterId: Long): Mono<Long> {
        val draftProduct = draftProductRepository
            .getDraftProduct(draftProductId, knitterId)
        return draftProduct
            .flatMap { draftProductRepository.delete(it) }
    }

    interface DraftProductRepository {
        fun getDraftProduct(id: Long, knitterId: Long): Mono<DraftProduct>
        fun findDraftProductsToCreate(knitterId: Long): Flux<DraftProduct>
        fun getDraftProductToUpdate(productId: Long, knitterId: Long): Mono<DraftProduct>
        fun create(draftProduct: DraftProduct): Mono<DraftProduct>
        fun update(draftProduct: DraftProduct): Mono<DraftProduct>
        fun delete(draftProduct: DraftProduct): Mono<Long>
    }

    interface ProductRepository {
        fun getProduct(id: Long, knitterId: Long): Mono<Product>
    }
}
