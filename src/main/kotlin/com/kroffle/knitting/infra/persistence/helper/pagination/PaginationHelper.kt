package com.kroffle.knitting.infra.persistence.helper.pagination

import com.kroffle.knitting.usecase.helper.pagination.type.Paging
import com.kroffle.knitting.usecase.helper.pagination.type.Sort
import org.springframework.data.domain.PageRequest

class PaginationHelper {
    companion object {
        fun makePageRequest(paging: Paging, sort: Sort): PageRequest = PageRequest.of(
            0,
            paging.count,
            sort.makeSort(),
        )
    }
}
