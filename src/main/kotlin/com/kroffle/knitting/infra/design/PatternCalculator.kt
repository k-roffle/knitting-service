package com.kroffle.knitting.infra.design

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.kroffle.knitting.domain.design.value.Gauge
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round
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
    private fun calculate(target: Int, origin: Gauge, my: Gauge, calculateType: CalculateType): Int {
        val calculatedRawGauge = when (calculateType.gaugeType) {
            GaugeType.Row -> target * my.rows / origin.rows
            GaugeType.Stitch -> target * my.stitches / origin.stitches
        }
        return when (calculateType.method) {
            Method.Round -> round(calculatedRawGauge)
            Method.RoundUp -> ceil(calculatedRawGauge)
            Method.RoundDown -> floor(calculatedRawGauge)
        }.toInt()
    }
    fun calculateGauge(origin: Gauge, my: Gauge): Block {
        var result = text
        for (range in inlineStyleRanges) {
            val startIdx = range.offset
            val endIdx = range.offset + range.length - 1
            val target = result.substring(startIdx, endIdx).toIntOrNull() ?: continue
            val calculateType = range.getCalculateType() ?: continue
            val calculatedGauge = calculate(target, origin, my, calculateType)
            result = result.replaceRange(startIdx, endIdx, calculatedGauge.toString())
        }
        return Block(key, result, type, depth, inlineStyleRanges, entityRanges, data)
    }
}

data class InlineStyleRange(
    val offset: Int,
    val length: Int,
    val style: String,
) {
    @JsonIgnore
    fun getCalculateType(): CalculateType? = CalculateType.findByKey(style)
}

enum class GaugeType {
    Stitch,
    Row
}

enum class Method {
    Round,
    RoundUp,
    RoundDown
}

enum class CalculateType(
    val gaugeType: GaugeType,
    val method: Method,
    val keys: List<String>
) {
    StitchRound(
        GaugeType.Stitch,
        Method.Round,
        listOf("STITCH_CALCULATE_ROUND", "STITCH_REPEAT_CALCULATE_ROUND"),
    ),
    StitchRoundDown(
        GaugeType.Stitch,
        Method.RoundDown,
        listOf("STITCH_CALCULATE_ROUND_DOWN", "STITCH_REPEAT_CALCULATE_ROUND_DOWN"),
    ),
    StitchRoundUp(
        GaugeType.Stitch,
        Method.RoundUp,
        listOf("STITCH_CALCULATE_ROUND_UP", "STITCH_REPEAT_CALCULATE_ROUND_UP"),
    ),
    RowRound(
        GaugeType.Row,
        Method.Round,
        listOf("ROW_CALCULATE_ROUND", "ROW_REPEAT_CALCULATE_ROUND"),
    ),
    RowRoundDown(
        GaugeType.Row,
        Method.RoundDown,
        listOf("ROW_CALCULATE_ROUND_DOWN", "ROW_REPEAT_CALCULATE_ROUND_DOWN"),
    );

    companion object {
        fun findByKey(key: String): CalculateType? {
            for (type in values()) {
                if (type.keys.contains(key))
                    return type
            }
            return null
        }
    }
}

class PatternCalculator {
    private val mapper: ObjectMapper = ObjectMapper().registerKotlinModule()

    private fun deserializePattern(pattern: DomainPattern): Pattern {
        return mapper.readValue(pattern.value, Pattern::class.java)
    }

    private fun serializePattern(pattern: Pattern): DomainPattern {
        return DomainPattern(mapper.writeValueAsString(pattern))
    }

    fun calculateGauge(target: DomainPattern, origin: Gauge, my: Gauge): DomainPattern {
        val deserializedTarget = deserializePattern(target)
        return serializePattern(deserializedTarget.calculateBlocks(origin, my))
    }
}
