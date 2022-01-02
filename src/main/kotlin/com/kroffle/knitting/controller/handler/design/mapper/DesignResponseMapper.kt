package com.kroffle.knitting.controller.handler.design.mapper

import com.kroffle.knitting.controller.handler.design.dto.MyDesign
import com.kroffle.knitting.controller.handler.design.dto.NewDesign
import com.kroffle.knitting.domain.design.entity.Design

object DesignResponseMapper {
    fun toNewDesignResponse(design: Design): NewDesign.Response =
        with(design) {
            NewDesign.Response(id = id!!)
        }

    fun toMyDesignResponse(design: Design): MyDesign.Response =
        with(design) {
            MyDesign.Response(
                id = id!!,
                name = name,
                yarn = yarn,
                coverImageUrl = coverImageUrl,
                tags = listOf(designType.tag, patternType.tag),
                createdAt = createdAt!!,
            )
        }
}
