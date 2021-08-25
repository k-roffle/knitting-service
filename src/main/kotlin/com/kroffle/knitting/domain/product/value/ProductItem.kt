package com.kroffle.knitting.domain.product.value

import com.kroffle.knitting.domain.product.enum.ProductItemType
import java.time.LocalDateTime

/* FIXME: #99 이슈 진행하며 id 제거가 가능하다면 제거, 불가능하다면 entity 로 변경합니다. */
abstract class ProductItem(
    val id: Long?,
    val itemId: Long,
    val createdAt: LocalDateTime?,
) {
    abstract val type: ProductItemType

    companion object {
        fun create(id: Long?, itemId: Long, createdAt: LocalDateTime?, type: ProductItemType): ProductItem {
            return when (type) {
                ProductItemType.DESIGN -> DesignProductItem(id, itemId, createdAt)
                ProductItemType.GOODS -> GoodsProductItem(id, itemId, createdAt)
            }
        }
    }
}
