package com.kroffle.knitting.data.entity.design

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("size")
class SizeEntity(
    @Id val id: UUID = UUID.randomUUID(),
    val totalLength: Double,
    val sleeveLength: Double,
    val shoulderWidth: Double,
    val bottomWidth: Double,
    val armholeDepth: Double,
)
