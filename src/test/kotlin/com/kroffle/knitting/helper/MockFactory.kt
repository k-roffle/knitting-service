package com.kroffle.knitting.helper

import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.helper.dto.MockProductData

class MockFactory {
    companion object {
        fun create(data: MockProductData): Product {
            return Product(
                id = data.id,
                knitterId = data.knitterId,
                name = data.name,
                fullPrice = data.fullPrice,
                discountPrice = data.discountPrice,
                representativeImageUrl = data.representativeImageUrl,
                specifiedSalesStartDate = data.specifiedSalesStartDate,
                specifiedSalesEndDate = data.specifiedSalesEndDate,
                tags = data.tags,
                content = data.content,
                inputStatus = data.inputStatus,
                items = data.items,
                createdAt = data.createdAt,
            )
        }
    }
}
