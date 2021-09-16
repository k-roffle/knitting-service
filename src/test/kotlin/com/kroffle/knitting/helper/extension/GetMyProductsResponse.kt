package com.kroffle.knitting.helper.extension

import com.kroffle.knitting.controller.handler.product.dto.GetMyProductsResponse

fun GetMyProductsResponse.like(other: GetMyProductsResponse): Boolean {
    if (this === other) return true
    tags.mapIndexed {
        index, tag ->
        if (other.tags[index] != tag) {
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
        inputStatus == other.inputStatus &&
        updatedAt == other.updatedAt
}
