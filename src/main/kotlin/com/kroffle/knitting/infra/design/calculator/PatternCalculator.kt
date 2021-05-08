package com.kroffle.knitting.infra.design.calculator

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.kroffle.knitting.domain.design.value.Gauge
import com.kroffle.knitting.domain.design.value.Pattern
import com.kroffle.knitting.infra.design.calculator.data.DraftPattern

class PatternCalculator {
    private val mapper: ObjectMapper = ObjectMapper().registerKotlinModule()

    private fun deserializePattern(pattern: Pattern): DraftPattern {
        return mapper.readValue(pattern.value, DraftPattern::class.java)
    }

    private fun serializePattern(pattern: DraftPattern): Pattern {
        return Pattern(mapper.writeValueAsString(pattern))
    }

    fun calculateGauge(target: Pattern, origin: Gauge, my: Gauge): Pattern {
        val deserializedTarget = deserializePattern(target)
        return serializePattern(deserializedTarget.calculateBlocks(origin, my))
    }
}
