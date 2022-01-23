package com.kroffle.knitting.controller.handler.product.mapper

import com.kroffle.knitting.controller.handler.product.dto.CreateProduct
import com.kroffle.knitting.controller.handler.product.dto.EditProductContent
import com.kroffle.knitting.controller.handler.product.dto.EditProductPackage
import com.kroffle.knitting.controller.handler.product.dto.RegisterProduct
import com.kroffle.knitting.controller.handler.product.dto.UpdateProduct
import com.kroffle.knitting.domain.product.value.ProductItem
import com.kroffle.knitting.domain.product.value.ProductTag
import com.kroffle.knitting.domain.value.Money
import com.kroffle.knitting.usecase.helper.pagination.type.Paging
import com.kroffle.knitting.usecase.helper.pagination.type.Sort
import com.kroffle.knitting.usecase.helper.pagination.type.SortDirection
import com.kroffle.knitting.usecase.product.dto.CreateProductData
import com.kroffle.knitting.usecase.product.dto.EditProductContentData
import com.kroffle.knitting.usecase.product.dto.EditProductPackageData
import com.kroffle.knitting.usecase.product.dto.GetMyProductData
import com.kroffle.knitting.usecase.product.dto.GetMyProductsData
import com.kroffle.knitting.usecase.product.dto.RegisterProductData
import com.kroffle.knitting.usecase.product.dto.UpdateProductData

object ProductRequestMapper {
    fun toEditProductPackageData(data: EditProductPackage.Request, knitterId: Long): EditProductPackageData =
        with(data) {
            EditProductPackageData(
                id = id,
                knitterId = knitterId,
                name = name,
                fullPrice = Money(fullPrice),
                discountPrice = Money(discountPrice),
                representativeImageUrl = representativeImageUrl,
                specifiedSalesStartDate = specifiedSalesStartDate,
                specifiedSalesEndDate = specifiedSalesEndDate,
                tags = tags.map { ProductTag(it) },
                items = designIds.map { ProductItem.create(it, ProductItem.Type.DESIGN) },
            )
        }

    fun toEditProductContentData(data: EditProductContent.Request, knitterId: Long): EditProductContentData =
        with(data) {
            EditProductContentData(
                id = id,
                knitterId = knitterId,
                content = content,
            )
        }

    fun toRegisterProductData(data: RegisterProduct.Request, knitterId: Long): RegisterProductData =
        with(data) {
            RegisterProductData(
                id = id,
                knitterId = knitterId,
            )
        }

    fun toCreateProductData(data: CreateProduct.Request, knitterId: Long): CreateProductData =
        with(data) {
            CreateProductData(
                knitterId = knitterId,
                name = name,
                fullPrice = Money(fullPrice),
                discountPrice = Money(discountPrice),
                representativeImageUrl = representativeImageUrl,
                specifiedSalesStartDate = specifiedSalesStartDate?.toLocalDate(),
                specifiedSalesEndDate = specifiedSalesEndDate?.toLocalDate(),
                content = content,
                tags = tags.map { ProductTag(it) },
                items = designIds.map { ProductItem.create(it, ProductItem.Type.DESIGN) },
                draftId = draftId,
            )
        }

    fun toUpdateProductData(data: UpdateProduct.Request, productId: Long, knitterId: Long): UpdateProductData =
        with(data) {
            UpdateProductData(
                id = productId,
                knitterId = knitterId,
                discountPrice = Money(discountPrice),
                specifiedSalesStartDate = specifiedSalesStartDate?.toLocalDate(),
                specifiedSalesEndDate = specifiedSalesEndDate?.toLocalDate(),
                content = content,
                tags = tags.map { ProductTag(it) },
                draftId = draftId,
            )
        }

    fun toGetMyProductData(productId: Long, knitterId: Long) =
        GetMyProductData(
            id = productId,
            knitterId = knitterId,
        )

    fun toGetMyProductsData(paging: Paging, knitterId: Long) =
        GetMyProductsData(
            knitterId = knitterId,
            paging = paging,
            sort = Sort("id", SortDirection.DESC),
        )
}
