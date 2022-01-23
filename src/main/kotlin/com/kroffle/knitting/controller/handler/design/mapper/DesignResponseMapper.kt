package com.kroffle.knitting.controller.handler.design.mapper

import com.kroffle.knitting.controller.handler.design.dto.MyDesigns
import com.kroffle.knitting.controller.handler.design.dto.NewDesign
import com.kroffle.knitting.controller.handler.design.dto.UpdateDesign
import com.kroffle.knitting.domain.design.entity.Design

object DesignResponseMapper {
    fun toNewDesignResponse(design: Design): NewDesign.Response =
        with(design) {
            NewDesign.Response(id = id!!)
        }

    fun toUpdateDesignResponse(design: Design): UpdateDesign.Response =
        with(design) {
            UpdateDesign.Response(id = id!!)
        }

    fun toMyDesignsResponse(design: Design): MyDesigns.Response =
        with(design) {
            MyDesigns.Response(
                id = id!!,
                name = name,
                yarn = yarn,
                coverImageUrl = coverImageUrl,
                tags = listOf(designType.tag, patternType.tag),
                createdAt = createdAt!!,
            )
        }
}
