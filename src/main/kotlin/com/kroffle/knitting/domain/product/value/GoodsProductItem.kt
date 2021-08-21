package com.kroffle.knitting.domain.product.value

import com.kroffle.knitting.domain.product.enum.ProductItemType
import java.time.LocalDateTime

class GoodsProductItem(
    id: Long?,
    itemId: Long,
    createdAt: LocalDateTime?,
) : ProductItem(id, itemId, createdAt) {
    override val type: ProductItemType
        get() = ProductItemType.GOODS
}
