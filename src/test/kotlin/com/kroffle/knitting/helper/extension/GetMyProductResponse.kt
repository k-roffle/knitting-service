package com.kroffle.knitting.helper.extension

import com.kroffle.knitting.controller.handler.product.dto.GetMyProduct

fun GetMyProduct.Response.like(other: GetMyProduct.Response): Boolean {
    if (this === other) return true
    tags.mapIndexed { index, tag ->
        if (other.tags[index] != tag) {
            return false
        }
    }
    itemIds.mapIndexed { index, item ->
        if (other.itemIds[index] != item) {
            return false
        }
    }
    return id == other.id &&
        name == other.name &&
        fullPrice == other.fullPrice &&
        discountPrice == other.discountPrice &&
        representativeImageUrl == other.representativeImageUrl &&
        specifiedSalesStartDate == other.specifiedSalesStartDate &&
        specifiedSalesEndDate == other.specifiedSalesEndDate &&
        content == other.content &&
        inputStatus == other.inputStatus &&
        createdAt == other.createdAt &&
        updatedAt == other.updatedAt
}
