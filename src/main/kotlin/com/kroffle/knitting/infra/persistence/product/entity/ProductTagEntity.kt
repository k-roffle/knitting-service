package com.kroffle.knitting.infra.persistence.product.entity

import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.domain.product.value.ProductTag
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime

@Table("product_tag")
class ProductTagEntity(
    @Id private var id: Long? = null,
    private val productId: Long,
    private val name: String,
    private val createdAt: OffsetDateTime = OffsetDateTime.now(),
) {
    fun toTag() = ProductTag(
        name = name,
        createdAt = createdAt,
    )
    fun getForeignKey(): Long = productId
}

fun Product.toProductTagEntities(productId: Long): List<ProductTagEntity> =
    this.tags.map { tag ->
        ProductTagEntity(
            id = null,
            productId = this.id ?: productId,
            name = tag.name,
            createdAt = tag.createdAt ?: OffsetDateTime.now(),
        )
    }
