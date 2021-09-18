package com.kroffle.knitting.infra.persistence.product.entity

import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.domain.product.enum.ProductItemType
import com.kroffle.knitting.domain.product.value.ProductItem
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime

@Table("product_item")
class ProductItemEntity(
    @Id private var id: Long? = null,
    private val productId: Long,
    private val itemId: Long,
    private val type: ProductItemType,
    private val createdAt: OffsetDateTime = OffsetDateTime.now(),
) {
    fun toItem() = ProductItem.create(itemId, createdAt, type)
    fun getForeignKey(): Long = productId
}

fun Product.toProductItemEntities(productId: Long): List<ProductItemEntity> =
    this.items.map {
        item ->
        ProductItemEntity(
            productId = this.id ?: productId,
            itemId = item.itemId,
            type = item.type,
            createdAt = item.createdAt ?: OffsetDateTime.now(),
        )
    }
