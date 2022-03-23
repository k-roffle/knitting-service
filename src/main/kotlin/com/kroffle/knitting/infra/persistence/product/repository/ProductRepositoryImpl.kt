package com.kroffle.knitting.infra.persistence.product.repository

import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.infra.persistence.exception.NotFoundEntity
import com.kroffle.knitting.infra.persistence.helper.pagination.PaginationHelper
import com.kroffle.knitting.infra.persistence.product.entity.ProductEntity
import com.kroffle.knitting.infra.persistence.product.entity.ProductItemEntity
import com.kroffle.knitting.infra.persistence.product.entity.ProductTagEntity
import com.kroffle.knitting.infra.persistence.product.entity.toProductEntity
import com.kroffle.knitting.infra.persistence.product.entity.toProductItemEntities
import com.kroffle.knitting.infra.persistence.product.entity.toProductTagEntities
import com.kroffle.knitting.usecase.helper.pagination.type.Paging
import com.kroffle.knitting.usecase.helper.pagination.type.Sort
import com.kroffle.knitting.usecase.helper.pagination.type.SortDirection
import com.kroffle.knitting.usecase.repository.ProductRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.stream.Collectors.toList

@Repository
class ProductRepositoryImpl(
    private val productRepository: R2DBCProductRepository,
    private val productTagRepository: R2DBCProductTagRepository,
    private val productItemRepository: R2DBCProductItemRepository,
) : ProductRepository {
    private fun findById(id: Long): Mono<Product> {
        val tags = productTagRepository
            .findAllByProductId(id)
            .map { it.toTag() }
            .collect(toList())

        val items = productItemRepository
            .findAllByProductId(id)
            .map { it.toItem() }
            .collect(toList())

        val product = productRepository
            .findById(id)

        return Mono.zip(product, tags, items)
            .map { it.t1.toProduct(it.t2, it.t3) }
    }

    private fun getProductAggregates(products: Flux<ProductEntity>): Flux<Product> {
        val productIds: Mono<List<Long>> =
            products
                .map { product -> product.getNotNullId() }
                .collect(toList())

        val tagMap: Mono<Map<Long, Collection<ProductTagEntity>>> =
            productIds
                .flatMap {
                    productTagRepository
                        .findAllByProductIdIn(it)
                        .collectMultimap { tag -> tag.getForeignKey() }
                }
        val itemMap: Mono<Map<Long, Collection<ProductItemEntity>>> =
            productIds
                .flatMap {
                    productItemRepository
                        .findAllByProductIdIn(it)
                        .collectMultimap { item -> item.getForeignKey() }
                }

        return Mono.zip(tagMap, itemMap)
            .flatMapMany {
                products
                    .map { product ->
                        val productId = product.getNotNullId()
                        product.toProduct(
                            it.t1[productId]?.map { tag -> tag.toTag() } ?: listOf(),
                            it.t2[productId]?.map { item -> item.toItem() } ?: listOf(),
                        )
                    }
            }
    }

    private fun save(product: Product): Mono<Product> {
        return productRepository
            .save(product.toProductEntity())
            .flatMap { productEntity ->
                val productId: Long = productEntity.getNotNullId()

                val tags = productTagRepository
                    .deleteByProductId(productId)
                    .flatMap {
                        productTagRepository
                            .saveAll(product.toProductTagEntities(productId))
                            .map { it.toTag() }
                            .collect(toList())
                    }

                val items = productItemRepository
                    .deleteByProductId(productId)
                    .flatMap {
                        productItemRepository
                            .saveAll(product.toProductItemEntities(productId))
                            .map { it.toItem() }
                            .collect(toList())
                    }

                Mono.zip(tags, items)
                    .map { productEntity.toProduct(it.t1, it.t2) }
            }
    }

    override fun createProduct(product: Product): Mono<Product> = save(product)

    override fun updateProduct(product: Product): Mono<Product> = save(product)

    override fun getProductsByKnitterId(knitterId: Long, paging: Paging, sort: Sort): Flux<Product> {
        val pageRequest = PaginationHelper.makePageRequest(paging, sort)

        val products: Flux<ProductEntity> = when (sort.direction) {
            SortDirection.DESC ->
                if (paging.after != null) {
                    productRepository
                        .findAllByKnitterIdAndIdBefore(
                            knitterId = knitterId,
                            id = paging.after.toLong(),
                            pageable = pageRequest,
                        )
                } else {
                    productRepository
                        .findAllByKnitterId(
                            knitterId = knitterId,
                            pageable = pageRequest,
                        )
                }
            else -> throw NotImplementedError()
        }
        return getProductAggregates(products)
    }

    override fun findRegisteredProduct(knitterId: Long): Flux<Product> {
        val products: Flux<ProductEntity> = productRepository.findAll()
        return getProductAggregates(products)
    }

    override fun countMyProducts(knitterId: Long): Mono<Int> =
        productRepository.countByKnitterId(knitterId)

    override fun countPurchasedProducts(knitterId: Long): Mono<Int> {
        // TODO("Not yet implemented")
        return Mono.just(0)
    }

    override fun getProduct(id: Long, knitterId: Long): Mono<Product> {
        return findById(id)
            .filter { it.knitterId == knitterId }
            .switchIfEmpty(Mono.error(NotFoundEntity(ProductEntity::class.java)))
    }
}
