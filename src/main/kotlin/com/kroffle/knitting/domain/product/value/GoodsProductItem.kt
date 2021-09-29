package com.kroffle.knitting.domain.product.value

import com.kroffle.knitting.domain.product.enum.ProductItemType

class GoodsProductItem(itemId: Long) : ProductItem(itemId) {
    override val type: ProductItemType
        get() = ProductItemType.GOODS
}
