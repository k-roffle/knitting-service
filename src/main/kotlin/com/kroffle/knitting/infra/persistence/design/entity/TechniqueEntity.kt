package com.kroffle.knitting.infra.persistence.design.entity

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.design.value.Technique
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("technique")
class TechniqueEntity(
    @Id private var id: Long? = null,
    private val designId: Long,
    private val name: String,
) {
    fun toTechnique() = Technique(name)

    fun getForeignKey() = designId
}

fun Design.toTechniqueEntities(designId: Long): List<TechniqueEntity> =
    this.techniques.map { technique ->
        TechniqueEntity(
            name = technique.name,
            designId = this.id ?: designId,
        )
    }
