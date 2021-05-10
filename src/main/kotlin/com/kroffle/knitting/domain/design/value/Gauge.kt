package com.kroffle.knitting.domain.design.value

class Gauge(val stitches: Double, val rows: Double) {
    init {
        require(this.stitches > 0 && this.rows > 0)
    }
}
