package com.kroffle.knitting.domain.product.entity

import com.kroffle.knitting.domain.product.exception.InvalidDiscountPrice
import com.kroffle.knitting.domain.product.exception.InvalidFullPrice
import com.kroffle.knitting.domain.product.exception.InvalidPeriod
import com.kroffle.knitting.domain.product.value.ProductItem
import com.kroffle.knitting.domain.product.value.ProductTag
import com.kroffle.knitting.domain.value.Money
import java.time.LocalDate
import java.time.OffsetDateTime

data class Product(
    val id: Long? = null,
    val knitterId: Long,
    val name: String,
    val fullPrice: Money,
    val discountPrice: Money,
    val representativeImageUrl: String,
    val specifiedSalesStartDate: LocalDate?,
    val specifiedSalesEndDate: LocalDate?,
    val content: String,
    val tags: List<ProductTag>,
    val items: List<ProductItem>,
    val createdAt: OffsetDateTime?,
    val updatedAt: OffsetDateTime?,
) {
    init {
        require(
            specifiedSalesStartDate == null ||
                specifiedSalesEndDate == null ||
                specifiedSalesStartDate <= specifiedSalesEndDate
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
    }

    private val salesStatus: SalesStatus
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
        get() = salesStatus == SalesStatus.ON_SALES

    fun update(
        discountPrice: Money,
        specifiedSalesStartDate: LocalDate?,
        specifiedSalesEndDate: LocalDate?,
        content: String,
        tags: List<ProductTag>,
    ) = this.copy(
        discountPrice = discountPrice,
        specifiedSalesStartDate = specifiedSalesStartDate,
        specifiedSalesEndDate = specifiedSalesEndDate,
        content = content,
        tags = tags,
        updatedAt = OffsetDateTime.now(),
    )

    enum class SalesStatus {
        RESERVED,
        ON_SALES,
        COMPLETED,
    }

    companion object {
        fun new(
            knitterId: Long,
            name: String,
            fullPrice: Money,
            discountPrice: Money,
            representativeImageUrl: String,
            specifiedSalesStartDate: LocalDate?,
            specifiedSalesEndDate: LocalDate?,
            content: String,
            tags: List<ProductTag>,
            items: List<ProductItem>,
        ): Product {
            return Product(
                id = null,
                knitterId = knitterId,
                name = name,
                fullPrice = fullPrice,
                discountPrice = discountPrice,
                representativeImageUrl = representativeImageUrl,
                specifiedSalesStartDate = specifiedSalesStartDate,
                specifiedSalesEndDate = specifiedSalesEndDate,
                content = content,
                tags = tags,
                items = items,
                null,
                null,
            )
        }
    }
}
