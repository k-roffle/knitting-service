package com.kroffle.knitting.infra.persistence.product.entity

import com.kroffle.knitting.domain.product.entity.Product
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("product_tag")
class ProductTagEntity(
    private val id: Long? = null,
    private val productId: Long,
    private val tag: String,
    private val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    fun toTag() = this.tag
}

fun Product.toProductTagEntities(): List<ProductTagEntity> =
    this.tags.map {
        tag ->
        ProductTagEntity(
            id = null,
            productId = this.id!!,
            tag = tag,
        )
    }
