package com.kroffle.knitting.domain.product.value

import com.kroffle.knitting.domain.product.enum.ProductItemType

abstract class ProductItem(val itemId: Long) {
    abstract val type: ProductItemType

    companion object {
        fun create(itemId: Long, type: ProductItemType): ProductItem {
            return when (type) {
                ProductItemType.DESIGN -> DesignProductItem(itemId)
                ProductItemType.GOODS -> GoodsProductItem(itemId)
            }
        }
    }
}
