package com.kroffle.knitting.domain.product.value

import com.kroffle.knitting.domain.product.enum.ProductItemType
import java.time.LocalDateTime

class GoodsProductItem(
    itemId: Long,
    createdAt: LocalDateTime?,
) : ProductItem(itemId, createdAt) {
    override val type: ProductItemType
        get() = ProductItemType.GOODS
}
