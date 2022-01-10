package com.kroffle.knitting.controller.handler.design.mapper

import com.kroffle.knitting.controller.handler.design.dto.NewDesign
import com.kroffle.knitting.controller.handler.design.dto.SizeDto
import com.kroffle.knitting.controller.handler.design.dto.UpdateDesign
import com.kroffle.knitting.domain.design.value.Gauge
import com.kroffle.knitting.domain.design.value.Length
import com.kroffle.knitting.domain.design.value.Pattern
import com.kroffle.knitting.domain.design.value.Size
import com.kroffle.knitting.domain.design.value.Technique
import com.kroffle.knitting.domain.value.Money
import com.kroffle.knitting.usecase.design.dto.CreateDesignData
import com.kroffle.knitting.usecase.design.dto.MyDesignFilter
import com.kroffle.knitting.usecase.design.dto.UpdateDesignData
import com.kroffle.knitting.usecase.helper.pagination.type.Paging
import com.kroffle.knitting.usecase.helper.pagination.type.Sort
import com.kroffle.knitting.usecase.helper.pagination.type.SortDirection

object DesignRequestMapper {
    fun toCreateDesignData(data: NewDesign.Request, knitterId: Long): CreateDesignData =
        with(data) {
            CreateDesignData(
                knitterId = knitterId,
                name = name,
                designType = designType,
                patternType = patternType,
                gauge = Gauge(
                    stitches = stitches,
                    rows = rows,
                ),
                size = toDomainFromDto(size),
                needle = needle,
                yarn = yarn,
                extra = extra,
                price = Money(price),
                pattern = Pattern(pattern),
                description = description,
                targetLevel = targetLevel,
                coverImageUrl = coverImageUrl,
                techniques = techniques.map { technique -> Technique(technique) },
                draftId = draftId,
            )
        }

    fun toUpdateDesignData(data: UpdateDesign.Request, designId: Long, knitterId: Long): UpdateDesignData =
        with(data) {
            UpdateDesignData(
                id = designId,
                knitterId = knitterId,
                designType = designType,
                patternType = patternType,
                gauge = Gauge(
                    stitches = stitches,
                    rows = rows,
                ),
                size = toDomainFromDto(size),
                needle = needle,
                yarn = yarn,
                extra = extra,
                pattern = Pattern(pattern),
                description = description,
                targetLevel = targetLevel,
                techniques = techniques.map { Technique(it) },
                draftId = draftId,
            )
        }

    fun toMyDesignFilter(paging: Paging, knitterId: Long) =
        MyDesignFilter(
            knitterId,
            paging,
            Sort("id", SortDirection.DESC),
        )

    private fun toDomainFromDto(size: SizeDto): Size =
        with(size) {
            Size(
                totalLength = Length(value = totalLength, unit = sizeUnit),
                sleeveLength = Length(value = sleeveLength, unit = sizeUnit),
                shoulderWidth = Length(value = shoulderWidth, unit = sizeUnit),
                bottomWidth = Length(value = bottomWidth, unit = sizeUnit),
                armholeDepth = Length(value = armholeDepth, unit = sizeUnit),
            )
        }
}
