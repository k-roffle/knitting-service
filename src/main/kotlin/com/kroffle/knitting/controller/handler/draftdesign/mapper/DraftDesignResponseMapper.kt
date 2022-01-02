package com.kroffle.knitting.controller.handler.draftdesign.mapper

import com.kroffle.knitting.controller.handler.draftdesign.dto.DeleteDraftDesign
import com.kroffle.knitting.controller.handler.draftdesign.dto.GetMyDraftDesign
import com.kroffle.knitting.controller.handler.draftdesign.dto.GetMyDraftDesignToUpdate
import com.kroffle.knitting.controller.handler.draftdesign.dto.GetMyDraftDesigns
import com.kroffle.knitting.controller.handler.draftdesign.dto.SaveDraftDesign
import com.kroffle.knitting.domain.draftdesign.entity.DraftDesign

object DraftDesignResponseMapper {
    fun toSaveDraftDesignResponse(draftDesign: DraftDesign): SaveDraftDesign.Response =
        with(draftDesign) {
            SaveDraftDesign.Response(id = id!!)
        }

    fun toGetMyDraftDesignsResponse(draftDesign: DraftDesign): GetMyDraftDesigns.Response =
        with(draftDesign) {
            GetMyDraftDesigns.Response(
                id = id!!,
                name = name,
                updatedAt = updatedAt!!,
            )
        }

    fun toGetMyDraftDesignResponse(draftDesign: DraftDesign): GetMyDraftDesign.Response =
        with(draftDesign) {
            GetMyDraftDesign.Response(
                id = id!!,
                value = value,
                updatedAt = updatedAt!!,
            )
        }

    fun toGetMyDraftDesignToUpdateResponse(draftDesign: DraftDesign): GetMyDraftDesignToUpdate.Response =
        with(draftDesign) {
            GetMyDraftDesignToUpdate.Response(
                id = id!!,
                value = value,
                updatedAt = updatedAt!!,
            )
        }

    fun toDeleteDraftDesignResponse(deletedId: Long): DeleteDraftDesign.Response =
        DeleteDraftDesign.Response(id = deletedId)
}
