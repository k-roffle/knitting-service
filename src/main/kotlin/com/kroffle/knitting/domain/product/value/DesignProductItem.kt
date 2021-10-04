package com.kroffle.knitting.domain.product.value

import com.kroffle.knitting.domain.product.enum.ProductItemType

class DesignProductItem(itemId: Long) : ProductItem(itemId) {
    override val type: ProductItemType
        get() = ProductItemType.DESIGN
}
