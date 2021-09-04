package com.kroffle.knitting.helper.extension

import com.kroffle.knitting.controller.handler.product.dto.GetMyProductResponse

fun GetMyProductResponse.like(other: GetMyProductResponse): Boolean {
    if (this === other) return true
    tags.mapIndexed {
        index, tag ->
        if (other.tags[index].tag != tag.tag) {
            return false
        }
    }
    items.mapIndexed {
        index, item ->
        if (other.items[index].itemId != item.itemId) {
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
