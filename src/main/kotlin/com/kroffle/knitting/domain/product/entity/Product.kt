package com.kroffle.knitting.domain.product.entity

import com.kroffle.knitting.domain.product.enum.InputStatus
import com.kroffle.knitting.domain.product.enum.SalesStatus
import com.kroffle.knitting.domain.product.exception.InvalidDiscountPrice
import com.kroffle.knitting.domain.product.exception.InvalidFullPrice
import com.kroffle.knitting.domain.product.exception.InvalidInputStatus
import com.kroffle.knitting.domain.product.exception.InvalidPeriod
import com.kroffle.knitting.domain.product.exception.UnableToRegister
import com.kroffle.knitting.domain.product.value.ProductItem
import com.kroffle.knitting.domain.product.value.ProductTag
import com.kroffle.knitting.domain.value.Money
import java.time.LocalDate
import java.time.OffsetDateTime

class Product(
    val id: Long? = null,
    val knitterId: Long,
    val name: String,
    val fullPrice: Money,
    val discountPrice: Money,
    val representativeImageUrl: String,
    val specifiedSalesStartDate: LocalDate?,
    val specifiedSalesEndDate: LocalDate?,
    val tags: List<ProductTag>,
    val content: String?,
    val inputStatus: InputStatus,
    val items: List<ProductItem>,
    val createdAt: OffsetDateTime?,
    val updatedAt: OffsetDateTime?,
) {
    init {
        require(
            specifiedSalesStartDate == null ||
                specifiedSalesEndDate == null ||
                specifiedSalesStartDate > specifiedSalesEndDate
        ) {
            throw InvalidPeriod()
        }

        require(
            Money.ZERO < discountPrice &&
                discountPrice <= fullPrice
        ) {
            throw InvalidDiscountPrice()
        }

        require(fullPrice > Money.ZERO) {
            throw InvalidFullPrice()
        }

        require(inputStatus == InputStatus.DRAFT || content != null) {
            throw InvalidInputStatus()
        }
    }

    val salesStatus: SalesStatus
        get() {
            val today = LocalDate.now()
            val start = specifiedSalesStartDate ?: LocalDate.MIN
            val end = specifiedSalesEndDate ?: LocalDate.MAX
            return if (start > today) {
                SalesStatus.RESERVED
            } else if (end < today) {
                SalesStatus.COMPLETED
            } else {
                SalesStatus.ON_SALES
            }
        }

    val onList: Boolean
        get() =
            salesStatus == SalesStatus.ON_SALES &&
                inputStatus == InputStatus.REGISTERED

    fun draftPackage(
        knitterId: Long,
        name: String,
        fullPrice: Money,
        discountPrice: Money,
        representativeImageUrl: String,
        specifiedSalesStartDate: LocalDate?,
        specifiedSalesEndDate: LocalDate?,
        tags: List<ProductTag>,
        items: List<ProductItem>,
    ): Product {
        return Product(
            id,
            knitterId,
            name,
            fullPrice,
            discountPrice,
            representativeImageUrl,
            specifiedSalesStartDate,
            specifiedSalesEndDate,
            tags,
            content,
            inputStatus,
            items,
            createdAt,
            OffsetDateTime.now(),
        )
    }

    fun draftContent(newContent: String): Product {
        return Product(
            id,
            knitterId,
            name,
            fullPrice,
            discountPrice,
            representativeImageUrl,
            specifiedSalesStartDate,
            specifiedSalesEndDate,
            tags,
            newContent,
            inputStatus,
            items,
            createdAt,
            OffsetDateTime.now(),
        )
    }

    fun register(): Product {
        if (content == null) {
            throw UnableToRegister()
        }
        return Product(
            id,
            knitterId,
            name,
            fullPrice,
            discountPrice,
            representativeImageUrl,
            specifiedSalesStartDate,
            specifiedSalesEndDate,
            tags,
            content,
            InputStatus.REGISTERED,
            items,
            createdAt,
            OffsetDateTime.now(),
        )
    }

    companion object {
        fun draftProductPackage(
            knitterId: Long,
            name: String,
            fullPrice: Money,
            discountPrice: Money,
            representativeImageUrl: String,
            specifiedSalesStartDate: LocalDate?,
            specifiedSalesEndDate: LocalDate?,
            tags: List<ProductTag>,
            items: List<ProductItem>,
        ): Product {
            return Product(
                null,
                knitterId,
                name,
                fullPrice,
                discountPrice,
                representativeImageUrl,
                specifiedSalesStartDate,
                specifiedSalesEndDate,
                tags,
                null,
                InputStatus.DRAFT,
                items,
                null,
                OffsetDateTime.now(),
            )
        }
    }
}
