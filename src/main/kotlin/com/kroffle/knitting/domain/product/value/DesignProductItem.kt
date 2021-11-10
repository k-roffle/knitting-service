package com.kroffle.knitting.domain.product.value

class DesignProductItem(itemId: Long) : ProductItem(itemId) {
    override val type: ProductItem.Type
        get() = ProductItem.Type.DESIGN
}
