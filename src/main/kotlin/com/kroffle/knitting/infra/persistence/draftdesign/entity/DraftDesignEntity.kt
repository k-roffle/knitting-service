package com.kroffle.knitting.infra.persistence.draftdesign.entity

import com.kroffle.knitting.domain.draftdesign.entity.DraftDesign
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime

@Table("draft_design")
class DraftDesignEntity(
    @Id private var id: Long?,
    private val knitterId: Long,
    private val designId: Long?,
    private val value: String,
    private val createdAt: OffsetDateTime = OffsetDateTime.now(),
    private val updatedAt: OffsetDateTime = OffsetDateTime.now(),
) {
    fun toDraftDesign(): DraftDesign =
        DraftDesign(
            id = this.id,
            knitterId = this.knitterId,
            designId = this.designId,
            value = this.value,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
        )
}

fun DraftDesign.toDraftDesignEntity(): DraftDesignEntity =
    DraftDesignEntity(
        id = this.id,
        knitterId = this.knitterId,
        designId = this.designId,
        value = this.value,
        createdAt = this.createdAt ?: OffsetDateTime.now(),
        updatedAt = this.updatedAt ?: OffsetDateTime.now(),
    )
