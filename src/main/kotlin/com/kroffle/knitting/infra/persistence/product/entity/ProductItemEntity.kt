package com.kroffle.knitting.infra.persistence.product.entity

import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.domain.product.value.ProductItem
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("product_item")
class ProductItemEntity(
    @Id private var id: Long? = null,
    private val productId: Long,
    private val itemId: Long,
    private val type: ProductItem.Type,
) {
    fun toItem() = ProductItem.create(itemId, type)
    fun getForeignKey(): Long = productId
}

fun Product.toProductItemEntities(productId: Long): List<ProductItemEntity> =
    this.items.map { item ->
        ProductItemEntity(
            productId = this.id ?: productId,
            itemId = item.itemId,
            type = item.type,
        )
    }
