package com.kroffle.knitting.controller.handler.draftdesign.mapper

import com.kroffle.knitting.controller.handler.draftdesign.dto.SaveDraftDesign
import com.kroffle.knitting.usecase.draftdesign.dto.SaveDraftDesignData

object DraftDesignRequestMapper {
    fun toSaveDraftDesignData(
        data: SaveDraftDesign.Request,
        knitterId: Long,
    ): SaveDraftDesignData =
        with(data) {
            SaveDraftDesignData(
                id = id,
                knitterId = knitterId,
                designId = designId,
                value = value,
            )
        }
}
