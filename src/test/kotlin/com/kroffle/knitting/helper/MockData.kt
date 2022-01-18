package com.kroffle.knitting.helper

import com.kroffle.knitting.domain.design.entity.Design.DesignType
import com.kroffle.knitting.domain.design.entity.Design.LevelType
import com.kroffle.knitting.domain.design.entity.Design.PatternType
import com.kroffle.knitting.domain.design.value.Gauge
import com.kroffle.knitting.domain.design.value.Length
import com.kroffle.knitting.domain.design.value.Pattern
import com.kroffle.knitting.domain.design.value.Size
import com.kroffle.knitting.domain.design.value.Technique
import com.kroffle.knitting.domain.product.entity.Product.InputStatus
import com.kroffle.knitting.domain.product.value.ProductItem
import com.kroffle.knitting.domain.product.value.ProductTag
import com.kroffle.knitting.domain.value.Money
import java.time.LocalDate
import java.time.OffsetDateTime

object MockData {
    data class Product(
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
        val createdAt: OffsetDateTime? = OffsetDateTime.now(),
        val updatedAt: OffsetDateTime? = OffsetDateTime.now(),
    )

    data class Design(
        val id: Long,
        val knitterId: Long = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
        val name: String = "도안 이름",
        val designType: DesignType = DesignType.Sweater,
        val patternType: PatternType = PatternType.Image,
        val gauge: Gauge = Gauge(12.5, 13.0),
        val size: Size = Size(
            Length(65.0),
            Length(73.5),
            Length(32.0),
            Length(31.0),
            Length(28.5),
        ),
        val needle: String = "5.0mm 대바늘",
        val yarn: String = "캐시미어 100g",
        val extra: String? = null,
        val price: Money = Money.ZERO,
        val pattern: Pattern = Pattern("스웨터 뜨는 법"),
        val description: String = "캐시미어 스웨터",
        val targetLevel: LevelType = LevelType.EASY,
        val coverImageUrl: String = "https://mock.wordway.com/image.png",
        val techniques: List<Technique> = listOf(),
        val updatedAt: OffsetDateTime? = OffsetDateTime.now(),
        val createdAt: OffsetDateTime? = OffsetDateTime.now(),
    )
    data class DraftDesign(
        val id: Long,
        val knitterId: Long = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
        val value: String = """
            {
                "id": 1,
                "name": "도안",
            }
        """.trimIndent(),
        val designId: Long? = null,
        val createdAt: OffsetDateTime? = OffsetDateTime.now(),
        val updatedAt: OffsetDateTime? = OffsetDateTime.now(),
    )
}
