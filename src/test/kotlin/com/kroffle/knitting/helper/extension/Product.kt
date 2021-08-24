package com.kroffle.knitting.helper.extension

import com.kroffle.knitting.domain.product.entity.Product

fun Product.like(other: Product): Boolean {
    if (this === other) return true
    tags.mapIndexed {
        idx, tag ->
        if (other.tags[idx].tag != tag.tag) {
            return false
        }
    }
    items.mapIndexed {
        idx, item ->
        if (other.items[idx].itemId != item.itemId) {
            return false
        }
    }
    return id == other.id &&
        knitterId == other.knitterId &&
        name == other.name &&
        fullPrice.like(other.fullPrice) &&
        discountPrice.like(other.discountPrice) &&
        representativeImageUrl == other.representativeImageUrl &&
        specifiedSalesStartDate == other.specifiedSalesStartDate &&
        specifiedSalesEndDate == other.specifiedSalesEndDate &&
        content == other.content &&
        inputStatus == other.inputStatus &&
        createdAt == other.createdAt
}
