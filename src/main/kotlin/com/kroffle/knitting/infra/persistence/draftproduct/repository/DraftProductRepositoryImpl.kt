package com.kroffle.knitting.infra.persistence.draftproduct.repository

import com.kroffle.knitting.domain.draftproduct.entity.DraftProduct
import com.kroffle.knitting.infra.persistence.draftproduct.entity.toDraftProductEntity
import com.kroffle.knitting.infra.persistence.exception.NotFoundEntity
import com.kroffle.knitting.usecase.repository.DraftProductRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Repository
class DraftProductRepositoryImpl(
    private val draftProductRepository: R2DBCDraftProductRepository,
) : DraftProductRepository {
    override fun getDraftProduct(id: Long, knitterId: Long): Mono<DraftProduct> =
        draftProductRepository
            .findByIdAndKnitterId(id, knitterId)
            .switchIfEmpty(Mono.error(NotFoundEntity(DraftProduct::class.java)))
            .map { it.toDraftProduct() }

    override fun findDraftProductsToCreate(knitterId: Long): Flux<DraftProduct> =
        draftProductRepository
            .findByKnitterIdAndProductId(knitterId, null)
            .map { it.toDraftProduct() }

    override fun getDraftProductToUpdate(productId: Long, knitterId: Long): Mono<DraftProduct> =
        draftProductRepository
            .getByKnitterIdAndProductId(knitterId = knitterId, productId = productId)
            .switchIfEmpty(Mono.error(NotFoundEntity(DraftProduct::class.java)))
            .map { it.toDraftProduct() }

    private fun save(draftProduct: DraftProduct): Mono<DraftProduct> =
        draftProductRepository
            .save(draftProduct.toDraftProductEntity())
            .map { it.toDraftProduct() }

    override fun create(draftProduct: DraftProduct): Mono<DraftProduct> = save(draftProduct)

    override fun update(draftProduct: DraftProduct): Mono<DraftProduct> = save(draftProduct)

    override fun delete(draftProduct: DraftProduct): Mono<Long> =
        draftProductRepository
            .delete(draftProduct.toDraftProductEntity())
            .flatMap { Mono.just(draftProduct.id!!) }
            .switchIfEmpty { Mono.just(draftProduct.id!!) }
}
