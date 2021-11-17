package com.kroffle.knitting.domain.product.value

class GoodsProductItem(itemId: Long) : ProductItem(itemId) {
    override val type: ProductItem.Type
        get() = ProductItem.Type.GOODS
}
