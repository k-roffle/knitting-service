package com.kroffle.knitting.infra.design.calculator.data

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
    ),
    RowRoundUp(
        GaugeType.Row,
        Method.RoundUp,
        listOf("ROW_CALCULATE_ROUND_UP", "ROW_REPEAT_CALCULATE_ROUND_UP"),
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
