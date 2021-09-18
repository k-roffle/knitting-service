package com.kroffle.knitting.domain.product.value

import com.kroffle.knitting.domain.product.enum.ProductItemType
import java.time.OffsetDateTime

class GoodsProductItem(
    itemId: Long,
    createdAt: OffsetDateTime?,
) : ProductItem(itemId, createdAt) {
    override val type: ProductItemType
        get() = ProductItemType.GOODS
}
