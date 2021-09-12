package com.kroffle.knitting.domain.product.value

import com.kroffle.knitting.domain.product.enum.ProductItemType
import java.time.LocalDateTime

abstract class ProductItem(
    val itemId: Long,
    val createdAt: LocalDateTime?,
) {
    abstract val type: ProductItemType

    companion object {
        fun create(itemId: Long, createdAt: LocalDateTime?, type: ProductItemType): ProductItem {
            return when (type) {
                ProductItemType.DESIGN -> DesignProductItem(itemId, createdAt)
                ProductItemType.GOODS -> GoodsProductItem(itemId, createdAt)
            }
        }
    }
}
