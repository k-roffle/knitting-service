package com.kroffle.knitting.helper.dto

import com.kroffle.knitting.domain.product.enum.InputStatus
import com.kroffle.knitting.domain.product.value.ProductItem
import com.kroffle.knitting.domain.product.value.ProductTag
import com.kroffle.knitting.domain.value.Money
import com.kroffle.knitting.helper.WebTestClientHelper
import java.time.LocalDate
import java.time.LocalDateTime

data class MockProductData(
    val id: Long,
    val knitterId: Long = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
    val name: String = "상품 이름",
    val fullPrice: Money = Money(10000),
    val discountPrice: Money = Money(100),
    val representativeImageUrl: String = "http://test.knitting.com/image.jpg",
    val specifiedSalesStartDate: LocalDate? = null,
    val specifiedSalesEndDate: LocalDate? = null,
    val tags: List<ProductTag> = listOf(),
    val content: String? = null,
    val inputStatus: InputStatus = InputStatus.DRAFT,
    val items: List<ProductItem> = listOf(),
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = LocalDateTime.now(),
)
