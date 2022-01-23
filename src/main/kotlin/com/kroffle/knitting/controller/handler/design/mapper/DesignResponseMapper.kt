package com.kroffle.knitting.controller.handler.design.mapper

import com.kroffle.knitting.controller.handler.design.dto.MyDesign
import com.kroffle.knitting.controller.handler.design.dto.MyDesigns
import com.kroffle.knitting.controller.handler.design.dto.NewDesign
import com.kroffle.knitting.controller.handler.design.dto.SizeDto
import com.kroffle.knitting.controller.handler.design.dto.UpdateDesign
import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.design.value.Size

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

    fun toMyDesignResponse(design: Design): MyDesign.Response =
        with(design) {
            MyDesign.Response(
                id = id!!,
                name = name,
                designType = designType,
                patternType = patternType,
                gauge = gauge,
                size = toDtoFromDomain(size),
                needle = needle,
                yarn = yarn,
                extra = extra,
                price = price,
                pattern = pattern,
                description = description,
                targetLevel = targetLevel,
                coverImageUrl = coverImageUrl,
                techniques = techniques,
                updatedAt = updatedAt,
                createdAt = createdAt,
            )
        }

    private fun toDtoFromDomain(size: Size): SizeDto =
        with(size) {
            SizeDto(
                sizeUnit = totalLength.unit,
                totalLength = totalLength.value,
                sleeveLength = sleeveLength.value,
                shoulderWidth = shoulderWidth.value,
                bottomWidth = bottomWidth.value,
                armholeDepth = armholeDepth.value,
            )
        }
}
