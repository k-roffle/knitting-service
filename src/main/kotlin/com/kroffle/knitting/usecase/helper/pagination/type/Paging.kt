package com.kroffle.knitting.usecase.helper.pagination.type

import java.lang.IllegalArgumentException

class Paging(
    val after: String?,
    val count: Int,
) {
    init {
        require(this.count in 1..30) {
            throw IllegalArgumentException("Count must be between 1 and 30")
        }
    }
}
