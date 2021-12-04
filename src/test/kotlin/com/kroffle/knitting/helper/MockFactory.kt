package com.kroffle.knitting.helper

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.draftdesign.entity.DraftDesign
import com.kroffle.knitting.domain.product.entity.Product

class MockFactory {
    companion object {
        fun create(data: MockData.Product): Product =
            with(data) {
                Product(
                    id = id,
                    knitterId = knitterId,
                    name = name,
                    fullPrice = fullPrice,
                    discountPrice = discountPrice,
                    representativeImageUrl = representativeImageUrl,
                    specifiedSalesStartDate = specifiedSalesStartDate,
                    specifiedSalesEndDate = specifiedSalesEndDate,
                    tags = tags,
                    content = content,
                    inputStatus = inputStatus,
                    items = items,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                )
            }

        fun create(data: MockData.Design): Design =
            with(data) {
                Design(
                    id = id,
                    knitterId = knitterId,
                    name = name,
                    designType = designType,
                    patternType = patternType,
                    gauge = gauge,
                    size = size,
                    needle = needle,
                    yarn = yarn,
                    extra = extra,
                    price = price,
                    pattern = pattern,
                    description = description,
                    targetLevel = targetLevel,
                    coverImageUrl = coverImageUrl,
                    techniques = techniques,
                    createdAt = createdAt,
                )
            }

        fun create(data: MockData.DraftDesign): DraftDesign =
            with(data) {
                DraftDesign(
                    id = id,
                    knitterId = knitterId,
                    value = value,
                    designId = designId,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                )
            }
    }
}
