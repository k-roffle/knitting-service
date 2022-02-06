package com.kroffle.knitting.controller.handler.product.mapper

import com.kroffle.knitting.controller.handler.product.dto.CreateProduct
import com.kroffle.knitting.controller.handler.product.dto.EditProductContent
import com.kroffle.knitting.controller.handler.product.dto.EditProductPackage
import com.kroffle.knitting.controller.handler.product.dto.GetMyProduct
import com.kroffle.knitting.controller.handler.product.dto.GetMyProducts
import com.kroffle.knitting.controller.handler.product.dto.RegisterProduct
import com.kroffle.knitting.controller.handler.product.dto.UpdateProduct
import com.kroffle.knitting.domain.product.entity.Product

object ProductResponseMapper {
    fun toEditProductPackageResponse(product: Product): EditProductPackage.Response =
        with(product) {
            EditProductPackage.Response(
                id = id!!,
            )
        }

    fun toEditProductContentResponse(product: Product): EditProductContent.Response =
        with(product) {
            EditProductContent.Response(
                id = id!!,
            )
        }

    fun toRegisterProductResponse(product: Product): RegisterProduct.Response =
        with(product) {
            RegisterProduct.Response(
                id = id!!,
            )
        }

    fun toCreateProductResponse(product: Product): CreateProduct.Response =
        with(product) {
            CreateProduct.Response(
                id = id!!,
            )
        }

    fun toUpdateProductResponse(product: Product): UpdateProduct.Response =
        with(product) {
            UpdateProduct.Response(
                id = id!!,
            )
        }

    fun toGetMyProductResponse(product: Product): GetMyProduct.Response =
        with(product) {
            GetMyProduct.Response(
                id = id!!,
                name = name,
                fullPrice = fullPrice.value,
                discountPrice = discountPrice.value,
                representativeImageUrl = representativeImageUrl,
                specifiedSalesStartedAt = specifiedSalesStartedAt,
                specifiedSalesEndedAt = specifiedSalesEndedAt,
                tags = tags.map { it.name },
                content = content,
                inputStatus = "inputStatus",
                itemIds = items.map { it.itemId },
                createdAt = createdAt!!,
                updatedAt = updatedAt!!,
            )
        }

    fun toGetMyProductsResponse(product: Product): GetMyProducts.Response =
        with(product) {
            GetMyProducts.Response(
                id = id!!,
                name = name,
                fullPrice = fullPrice.value,
                discountPrice = discountPrice.value,
                representativeImageUrl = representativeImageUrl,
                specifiedSalesStartedAt = specifiedSalesStartedAt,
                specifiedSalesEndedAt = specifiedSalesEndedAt,
                tags = tags.map { it.name },
                inputStatus = "inputStatus",
                updatedAt = updatedAt,
            )
        }
}
