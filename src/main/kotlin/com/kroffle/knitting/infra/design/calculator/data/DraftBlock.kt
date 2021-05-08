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
    private fun extractTargetValue(range: DraftInlineStyleRange): Int? =
        text.substring(range.getStartIndex(), range.getEndIndex()).toIntOrNull()

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

    private fun replaceWithCalculatedValue(row: String, range: DraftInlineStyleRange, calculatedValue: Int) =
        row.replaceRange(range.getStartIndex(), range.getEndIndex(), calculatedValue.toString())

    fun calculateGauge(origin: Gauge, my: Gauge): DraftBlock {
        var targetRow = text
        for (range in inlineStyleRanges) {
            val targetValue = extractTargetValue(range) ?: continue
            val calculateType = range.getCalculateType() ?: continue
            val calculatedValue = calculate(targetValue, origin, my, calculateType)
            targetRow = replaceWithCalculatedValue(targetRow, range, calculatedValue)
        }
        return DraftBlock(key, targetRow, type, depth, inlineStyleRanges, entityRanges, data)
    }
}
