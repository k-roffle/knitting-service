package com.kroffle.knitting.infra.persistence.product.entity

import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.domain.product.value.ProductItem
import com.kroffle.knitting.domain.product.value.ProductTag
import com.kroffle.knitting.domain.value.Money
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime

@Table("product")
class ProductEntity(
    @Id private var id: Long? = null,
    private val knitterId: Long,
    private val name: String,
    private val fullPrice: Int,
    private val discountPrice: Int,
    private val representativeImageUrl: String,
    private val specifiedSalesStartedAt: OffsetDateTime?,
    private val specifiedSalesEndedAt: OffsetDateTime?,
    private val content: String,
    private val createdAt: OffsetDateTime = OffsetDateTime.now(),
    private val updatedAt: OffsetDateTime = OffsetDateTime.now(),
) {
    fun getNotNullId(): Long = id!!

    fun toProduct(tags: List<ProductTag>, items: List<ProductItem>): Product =
        Product(
            id = id,
            knitterId = knitterId,
            name = name,
            fullPrice = Money(fullPrice),
            discountPrice = Money(discountPrice),
            representativeImageUrl = representativeImageUrl,
            specifiedSalesStartedAt = specifiedSalesStartedAt,
            specifiedSalesEndedAt = specifiedSalesEndedAt,
            content = content,
            tags = tags,
            items = items,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
}

fun Product.toProductEntity() =
    ProductEntity(
        id = id,
        knitterId = knitterId,
        name = name,
        fullPrice = fullPrice.value,
        discountPrice = discountPrice.value,
        representativeImageUrl = representativeImageUrl,
        specifiedSalesStartedAt = specifiedSalesStartedAt,
        specifiedSalesEndedAt = specifiedSalesEndedAt,
        content = content,
        createdAt = this.createdAt ?: OffsetDateTime.now(),
        updatedAt = this.updatedAt ?: OffsetDateTime.now(),
    )
