package com.kroffle.knitting.infra.design.entity

import com.kroffle.knitting.domain.design.value.Length
import com.kroffle.knitting.domain.design.value.Size
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("size")
class SizeEntity(
    @Id val id: UUID = UUID.randomUUID(),
    private val totalLength: Double,
    private val sleeveLength: Double,
    private val shoulderWidth: Double,
    private val bottomWidth: Double,
    private val armholeDepth: Double,
) {
    fun toSize(): Size =
        Size(
            totalLength = Length(this.totalLength),
            sleeveLength = Length(this.sleeveLength),
            shoulderWidth = Length(this.shoulderWidth),
            bottomWidth = Length(this.bottomWidth),
            armholeDepth = Length(this.armholeDepth),
        )
}
