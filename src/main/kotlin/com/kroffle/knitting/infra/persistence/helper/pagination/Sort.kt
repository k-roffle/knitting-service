package com.kroffle.knitting.infra.persistence.helper.pagination

import com.kroffle.knitting.usecase.helper.pagination.type.Sort
import com.kroffle.knitting.usecase.helper.pagination.type.SortDirection
import org.springframework.data.domain.Sort as R2DBCSort

fun Sort.makeSort(): R2DBCSort {
    val sort = R2DBCSort.by(this.column)
    return when (this.direction) {
        SortDirection.ASC -> sort.ascending()
        SortDirection.DESC -> sort.descending()
    }
}
