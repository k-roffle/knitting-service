package com.kroffle.knitting.infra.calculator.dto

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.kroffle.knitting.domain.design.value.Gauge
import com.kroffle.knitting.domain.design.value.Pattern

data class DraftPattern(val blocks: List<DraftBlock>, val entityMap: JsonNode) {
    fun calculateBlocks(origin: Gauge, input: Gauge): DraftPattern {
        return DraftPattern(blocks.map { it.calculateGauge(origin, input) }, entityMap)
    }

    fun toPattern(mapper: ObjectMapper): Pattern =
        Pattern(mapper.writeValueAsString(this))
}

fun Pattern.toDraftPattern(mapper: ObjectMapper): DraftPattern =
    mapper.readValue(this.value, DraftPattern::class.java)
