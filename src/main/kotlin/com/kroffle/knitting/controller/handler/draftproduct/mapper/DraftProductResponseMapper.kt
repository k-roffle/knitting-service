package com.kroffle.knitting.controller.handler.draftproduct.mapper

import com.kroffle.knitting.controller.handler.draftproduct.dto.DeleteDraftProduct
import com.kroffle.knitting.controller.handler.draftproduct.dto.GetMyDraftProduct
import com.kroffle.knitting.controller.handler.draftproduct.dto.GetMyDraftProductToUpdate
import com.kroffle.knitting.controller.handler.draftproduct.dto.GetMyDraftProducts
import com.kroffle.knitting.controller.handler.draftproduct.dto.SaveDraftProduct
import com.kroffle.knitting.domain.draftproduct.entity.DraftProduct

object DraftProductResponseMapper {
    fun toSaveDraftProductResponse(draftProduct: DraftProduct): SaveDraftProduct.Response =
        with(draftProduct) {
            SaveDraftProduct.Response(id = id!!)
        }

    fun toGetMyDraftProductsResponse(draftProduct: DraftProduct): GetMyDraftProducts.Response =
        with(draftProduct) {
            GetMyDraftProducts.Response(
                id = id!!,
                name = name,
                updatedAt = updatedAt!!,
            )
        }

    fun toGetMyDraftProductResponse(draftProduct: DraftProduct): GetMyDraftProduct.Response =
        with(draftProduct) {
            GetMyDraftProduct.Response(
                id = id!!,
                value = value,
                updatedAt = updatedAt!!,
            )
        }

    fun toGetMyDraftProductToUpdateResponse(draftProduct: DraftProduct): GetMyDraftProductToUpdate.Response =
        with(draftProduct) {
            GetMyDraftProductToUpdate.Response(
                id = id!!,
                value = value,
                updatedAt = updatedAt!!,
            )
        }

    fun toDeleteDraftProductResponse(deletedId: Long): DeleteDraftProduct.Response =
        DeleteDraftProduct.Response(id = deletedId)
}
