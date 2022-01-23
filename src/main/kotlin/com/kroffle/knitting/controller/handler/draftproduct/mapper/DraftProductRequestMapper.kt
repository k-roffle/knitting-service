package com.kroffle.knitting.controller.handler.draftproduct.mapper

import com.kroffle.knitting.controller.handler.draftproduct.dto.SaveDraftProduct
import com.kroffle.knitting.usecase.draftproduct.dto.SaveDraftProductData

object DraftProductRequestMapper {
    fun toSaveDraftProductData(
        data: SaveDraftProduct.Request,
        knitterId: Long,
    ): SaveDraftProductData =
        with(data) {
            SaveDraftProductData(
                id = id,
                knitterId = knitterId,
                productId = productId,
                value = value,
            )
        }
}
