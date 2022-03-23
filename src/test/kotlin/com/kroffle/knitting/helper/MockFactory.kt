package com.kroffle.knitting.helper

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.draftdesign.entity.DraftDesign
import com.kroffle.knitting.domain.knitter.entity.Knitter
import com.kroffle.knitting.domain.product.entity.Product

class MockFactory {
    companion object {
        fun create(data: MockData.Knitter): Knitter =
            with(data) {
                Knitter(
                    id = id,
                    email = email,
                    name = name,
                    profileImageUrl = profileImageUrl,
                    createdAt = createdAt,
                )
            }

        fun create(data: MockData.Product): Product =
            with(data) {
                Product(
                    id = id,
                    knitterId = knitterId,
                    name = name,
                    fullPrice = fullPrice,
                    discountPrice = discountPrice,
                    representativeImageUrl = representativeImageUrl,
                    specifiedSalesStartedAt = specifiedSalesStartedAt,
                    specifiedSalesEndedAt = specifiedSalesEndedAt,
                    tags = tags,
                    content = content,
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
                    updatedAt = updatedAt,
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
