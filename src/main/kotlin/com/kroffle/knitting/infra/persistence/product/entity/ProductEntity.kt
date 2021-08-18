package com.kroffle.knitting.infra.persistence.product.entity

import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.domain.product.enum.InputStatus
import com.kroffle.knitting.domain.value.Money
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table("product")
class ProductEntity(
    private val id: Long? = null,
    private val knitterId: Long,
    private val name: String,
    private val netPrice: Int,
    private val discountPrice: Int,
    private val representativeImageUrl: String,
    private val specifiedSalesStartDate: LocalDate?,
    private val specifiedSalesEndDate: LocalDate?,
    private val content: String?,
    private val inputStatus: InputStatus,
    private val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    fun toProduct(
        tags: List<String>,
        goodsIds: List<Long>,
        designIds: List<Long>,
    ): Product =
        Product(
            id = id,
            knitterId = knitterId,
            name = name,
            netPrice = Money(netPrice),
            discountPrice = Money(discountPrice),
            representativeImageUrl = representativeImageUrl,
            specifiedSalesStartDate = specifiedSalesStartDate,
            specifiedSalesEndDate = specifiedSalesEndDate,
            content = content,
            inputStatus = inputStatus,
            tags = tags,
            goodsIds = goodsIds,
            designIds = designIds,
            createdAt = createdAt,
        )
}

fun Product.toProductEntity() =
    ProductEntity(
        id = id,
        knitterId = knitterId,
        name = name,
        netPrice = netPrice.value,
        discountPrice = discountPrice.value,
        representativeImageUrl = representativeImageUrl,
        specifiedSalesStartDate = specifiedSalesStartDate,
        specifiedSalesEndDate = specifiedSalesEndDate,
        content = content,
        inputStatus = inputStatus,
        createdAt = this.createdAt ?: LocalDateTime.now(),
    )