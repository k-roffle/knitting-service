package com.kroffle.knitting.domain.product.entity

import com.kroffle.knitting.domain.exception.InvalidDiscountPrice
import com.kroffle.knitting.domain.exception.InvalidPeriod
import com.kroffle.knitting.domain.exception.UnableToRegister
import com.kroffle.knitting.domain.product.enum.InputStatus
import com.kroffle.knitting.domain.product.enum.SalesStatus
import com.kroffle.knitting.domain.value.Money
import java.time.LocalDate
import java.time.LocalDateTime

class Product(
    val id: Long? = null,
    val knitterId: Long,
    val name: String,
    val fullPrice: Money,
    val discountPrice: Money,
    val representativeImageUrl: String,
    val specifiedSalesStartDate: LocalDate?,
    val specifiedSalesEndDate: LocalDate?,
    val tags: List<String>,
    val content: String?,
    val inputStatus: InputStatus,
    val goodsIds: List<Long>,
    val designIds: List<Long>,
    val createdAt: LocalDateTime?,
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
            Money.MIN < discountPrice &&
                discountPrice <= fullPrice
        ) {
            throw InvalidDiscountPrice()
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

    val itemIds: List<Long>
        get() = goodsIds + designIds

    fun writeContent(newContent: String): Product {
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
            goodsIds,
            designIds,
            createdAt,
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
            goodsIds,
            designIds,
            createdAt,
        )
    }

    companion object {
        fun draft(
            knitterId: Long,
            name: String,
            fullPrice: Money,
            discountPrice: Money,
            representativeImageUrl: String,
            specifiedSalesStartDate: LocalDate?,
            specifiedSalesEndDate: LocalDate?,
            tags: List<String>,
            goodsIds: List<Long>,
            designIds: List<Long>,
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
                goodsIds,
                designIds,
                null,
            )
        }
    }
}
