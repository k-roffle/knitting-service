package com.kroffle.knitting.infra.design.calculator.data

import com.fasterxml.jackson.databind.JsonNode
import com.kroffle.knitting.domain.design.value.Gauge

data class DraftPattern(val blocks: List<DraftBlock>, val entityMap: JsonNode) {
    fun calculateBlocks(origin: Gauge, my: Gauge): DraftPattern {
        return DraftPattern(blocks.map { it.calculateGauge(origin, my) }, entityMap)
    }
}
