package com.kroffle.knitting.infra.persistence.draftproduct.entity

import com.kroffle.knitting.domain.draftproduct.entity.DraftProduct
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime

@Table("draft_product")
class DraftProductEntity(
    @Id private var id: Long?,
    private val knitterId: Long,
    private val productId: Long?,
    private val value: String,
    private val createdAt: OffsetDateTime = OffsetDateTime.now(),
    private val updatedAt: OffsetDateTime = OffsetDateTime.now(),
) {
    fun toDraftProduct(): DraftProduct =
        DraftProduct(
            id = this.id,
            knitterId = this.knitterId,
            productId = this.productId,
            value = this.value,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
        )
}

fun DraftProduct.toDraftProductEntity(): DraftProductEntity =
    DraftProductEntity(
        id = this.id,
        knitterId = this.knitterId,
        productId = this.productId,
        value = this.value,
        createdAt = this.createdAt ?: OffsetDateTime.now(),
        updatedAt = this.updatedAt ?: OffsetDateTime.now(),
    )
