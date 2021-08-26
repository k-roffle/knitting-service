package com.kroffle.knitting.infra.persistence.product.entity

import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.domain.product.value.ProductTag
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("product_tag")
class ProductTagEntity(
    @Id private var id: Long? = null,
    private val productId: Long,
    private val tag: String,
    private val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    fun toTag() = ProductTag(
        id = id,
        tag = tag,
        createdAt = createdAt,
    )
    fun getForeignKey(): Long = productId
}

fun Product.toProductTagEntities(productId: Long): List<ProductTagEntity> =
    this.tags.map {
        tag ->
        ProductTagEntity(
            id = tag.id,
            productId = this.id ?: productId,
            tag = tag.tag,
            createdAt = tag.createdAt ?: LocalDateTime.now(),
        )
    }
