package com.kroffle.knitting.helper.extension

import com.kroffle.knitting.domain.product.entity.Product

fun Product.like(other: Product): Boolean {
    if (this === other) return true
    tags.mapIndexed {
        index, tag ->
        if (other.tags[index].name != tag.name) {
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
