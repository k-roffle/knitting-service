package com.kroffle.knitting.infra.design

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.kroffle.knitting.domain.design.value.Gauge
import kotlin.math.roundToInt
import com.kroffle.knitting.domain.design.value.Pattern as DomainPattern

data class Pattern(val blocks: List<Block>, val entityMap: JsonNode) {
    fun calculateBlocks(origin: Gauge, my: Gauge): Pattern {
        return Pattern(blocks.map { it.calculateGauge(origin, my) }, entityMap)
    }
}

data class Block(
    val key: String,
    val text: String,
    val type: String,
    val depth: Int,
    val inlineStyleRanges: List<InlineStyleRange>,
    val entityRanges: JsonNode,
    val data: JsonNode,
) {
    fun calculateGauge(origin: Gauge, my: Gauge): Block {
        var result = text
        for (range in inlineStyleRanges) {
            val startIdx = range.offset
            val endIdx = range.offset + range.length - 1
            val target = result.substring(startIdx, endIdx).toIntOrNull() ?: continue
            val calculatedGauge: Int = when (range.style) {
                "STITCH_CALCULATE_ROUND" -> (target * my.stitches / origin.stitches).roundToInt()
                "ROW_CALCULATE_ROUND" -> (target * my.rows / origin.rows).roundToInt()
                else -> target
            }
            result = result.replaceRange(startIdx, endIdx, calculatedGauge.toString())
        }
        return Block(key, result, type, depth, inlineStyleRanges, entityRanges, data)
    }
}

data class InlineStyleRange(
    val offset: Int,
    val length: Int,
    val style: String,
)

class PatternCalculator {
    private val mapper: ObjectMapper = ObjectMapper().registerKotlinModule()

    private fun deserializePattern(pattern: DomainPattern): Pattern {
        return mapper.readValue(pattern.value, Pattern::class.java)
    }

    private fun serializePattern(pattern: Pattern): DomainPattern {
        return DomainPattern(mapper.writeValueAsString(pattern))
    }

    fun calculateGauge(target: DomainPattern, origin: Gauge, my: Gauge): DomainPattern {
        val target = deserializePattern(target)
        return serializePattern(target.calculateBlocks(origin, my))
    }
}
