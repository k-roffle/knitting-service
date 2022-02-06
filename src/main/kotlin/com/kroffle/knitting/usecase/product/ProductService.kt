package com.kroffle.knitting.usecase.product

import com.kroffle.knitting.domain.draftproduct.entity.DraftProduct
import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.usecase.helper.pagination.type.Paging
import com.kroffle.knitting.usecase.helper.pagination.type.Sort
import com.kroffle.knitting.usecase.product.dto.CreateProductData
import com.kroffle.knitting.usecase.product.dto.EditProductContentData
import com.kroffle.knitting.usecase.product.dto.EditProductPackageData
import com.kroffle.knitting.usecase.product.dto.GetMyProductData
import com.kroffle.knitting.usecase.product.dto.GetMyProductsData
import com.kroffle.knitting.usecase.product.dto.RegisterProductData
import com.kroffle.knitting.usecase.product.dto.UpdateProductData
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val draftProductRepository: DraftProductRepository,
) {
    @Deprecated("This fun doesn't work", replaceWith = ReplaceWith("DraftProductService"))
    fun edit(data: EditProductPackageData): Mono<Product> =
        Mono.empty()

    @Deprecated("This fun doesn't work", replaceWith = ReplaceWith("DraftProductService"))
    fun edit(data: EditProductContentData): Mono<Product> =
        Mono.empty()

    @Deprecated("This fun doesn't work", replaceWith = ReplaceWith("DraftProductService"))
    fun register(data: RegisterProductData): Mono<Product> =
        Mono.empty()

    private fun createProduct(data: CreateProductData): Mono<Product> =
        with(data) {
            productRepository
                .createProduct(
                    Product.new(
                        knitterId = knitterId,
                        name = name,
                        fullPrice = fullPrice,
                        discountPrice = discountPrice,
                        representativeImageUrl = representativeImageUrl,
                        specifiedSalesStartedAt = specifiedSalesStartedAt,
                        specifiedSalesEndedAt = specifiedSalesEndedAt,
                        content = content,
                        tags = tags,
                        items = items,
                    )
                )
        }

    private fun updateProduct(data: UpdateProductData): Mono<Product> =
        with(data) {
            productRepository
                .getProduct(data.id, data.knitterId)
                .flatMap { product ->
                    productRepository.updateProduct(
                        product.update(
                            discountPrice = discountPrice,
                            specifiedSalesStartedAt = specifiedSalesStartedAt,
                            specifiedSalesEndedAt = specifiedSalesEndedAt,
                            content = content,
                            tags = tags,
                        )
                    )
                }
        }

    private fun deleteMyDraftProduct(draftId: Long?, knitterId: Long): Mono<Long> {
        if (draftId == null) return Mono.empty()
        return draftProductRepository
            .getDraftProduct(id = draftId, knitterId = knitterId)
            .flatMap { draftProductRepository.delete(it) }
    }

    fun create(data: CreateProductData): Mono<Product> =
        createProduct(data)
            .flatMap { product ->
                deleteMyDraftProduct(data.draftId, data.knitterId)
                    .map { product }
                    .defaultIfEmpty(product)
            }

    fun update(data: UpdateProductData): Mono<Product> {
        return updateProduct(data)
            .flatMap { design ->
                deleteMyDraftProduct(data.draftId, data.knitterId)
                    .map { design }
                    .defaultIfEmpty(design)
            }
    }

    fun get(data: GetMyProductData): Mono<Product> =
        productRepository
            .getProduct(data.id, data.knitterId)

    fun get(data: GetMyProductsData): Flux<Product> =
        productRepository
            .getProductsByKnitterId(data.knitterId, data.paging, data.sort)

    interface ProductRepository {
        fun createProduct(product: Product): Mono<Product>
        fun updateProduct(product: Product): Mono<Product>
        fun getProduct(id: Long, knitterId: Long): Mono<Product>
        fun getProductsByKnitterId(knitterId: Long, paging: Paging, sort: Sort): Flux<Product>
    }

    interface DraftProductRepository {
        fun getDraftProduct(id: Long, knitterId: Long): Mono<DraftProduct>
        fun delete(draftProduct: DraftProduct): Mono<Long>
    }
}
