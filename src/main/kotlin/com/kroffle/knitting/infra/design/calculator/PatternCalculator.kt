package com.kroffle.knitting.infra.design.calculator

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.kroffle.knitting.domain.design.value.Gauge
import com.kroffle.knitting.domain.design.value.Pattern
import com.kroffle.knitting.infra.design.calculator.data.toDraftPattern

class PatternCalculator {
    private val mapper: ObjectMapper = ObjectMapper().registerKotlinModule()

    fun calculateGauge(target: Pattern, origin: Gauge, my: Gauge): Pattern {
        val draftPattern = target.toDraftPattern(mapper)
        return draftPattern.calculateBlocks(origin, my).toPattern(mapper)
    }
}
