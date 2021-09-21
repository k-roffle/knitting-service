package com.kroffle.knitting.infra.persistence.design.entity

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.design.enum.SizeUnitType
import com.kroffle.knitting.domain.design.value.Length
import com.kroffle.knitting.domain.design.value.Size
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("size")
class SizeEntity(
    @Id private var id: Long? = null,
    private val designId: Long,
    private val totalLength: Double,
    private val sleeveLength: Double,
    private val shoulderWidth: Double,
    private val bottomWidth: Double,
    private val armholeDepth: Double,
) {
    fun toSize() =
        Size(
            totalLength = Length(totalLength, DEFAULT_UNIT),
            sleeveLength = Length(sleeveLength, DEFAULT_UNIT),
            shoulderWidth = Length(shoulderWidth, DEFAULT_UNIT),
            bottomWidth = Length(bottomWidth, DEFAULT_UNIT),
            armholeDepth = Length(armholeDepth, DEFAULT_UNIT),
        )

    companion object {
        private val DEFAULT_UNIT = SizeUnitType.Cm
    }
}

fun Design.toSizeEntity(designId: Long): SizeEntity =
    SizeEntity(
        designId = id ?: designId,
        totalLength = size.totalLength.value,
        sleeveLength = size.sleeveLength.value,
        shoulderWidth = size.shoulderWidth.value,
        bottomWidth = size.bottomWidth.value,
        armholeDepth = size.armholeDepth.value,
    )
