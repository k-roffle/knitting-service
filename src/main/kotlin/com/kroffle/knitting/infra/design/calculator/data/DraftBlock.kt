package com.kroffle.knitting.infra.design.calculator.data

import com.fasterxml.jackson.databind.JsonNode
import com.kroffle.knitting.domain.design.value.Gauge
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round

data class DraftBlock(
    val key: String,
    val text: String,
    val type: String,
    val depth: Int,
    val inlineStyleRanges: List<DraftInlineStyleRange>,
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
    fun calculateGauge(origin: Gauge, my: Gauge): DraftBlock {
        var result = text
        for (range in inlineStyleRanges) {
            val startIdx = range.offset
            val endIdx = range.offset + range.length - 1
            val target = result.substring(startIdx, endIdx).toIntOrNull() ?: continue
            val calculateType = range.getCalculateType() ?: continue
            val calculatedGauge = calculate(target, origin, my, calculateType)
            result = result.replaceRange(startIdx, endIdx, calculatedGauge.toString())
        }
        return DraftBlock(key, result, type, depth, inlineStyleRanges, entityRanges, data)
    }
}
