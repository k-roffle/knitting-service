package com.kroffle.knitting.infra.persistence.product.entity

import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.infra.persistence.type.ProductItemType
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("product_item")
class ProductItemEntity(
    private val id: Long? = null,
    private val productId: Long,
    private val itemId: Long,
    private val type: ProductItemType,
    private val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    fun toItemId() = this.itemId
}

fun Product.toProductItemEntities(): List<ProductItemEntity> =
    (
        this.designIds.map {
            itemId ->
            ProductItemEntity(
                id = null,
                productId = this.id!!,
                itemId = itemId,
                type = ProductItemType.DESIGN,
            )
        } +
            this.goodsIds.map {
                itemId ->
                ProductItemEntity(
                    id = null,
                    productId = this.id!!,
                    itemId = itemId,
                    type = ProductItemType.GOODS,
                )
            }
        )
