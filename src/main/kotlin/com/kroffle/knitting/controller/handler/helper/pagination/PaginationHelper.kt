package com.kroffle.knitting.controller.handler.helper.pagination

import com.kroffle.knitting.usecase.helper.pagination.type.Paging
import org.springframework.web.reactive.function.server.ServerRequest

class PaginationHelper {
    companion object {
        private fun getValue(request: ServerRequest, key: String): String? {
            val value = request.queryParam(key)
            return if (value.isEmpty) null
            else value.get()
        }

        private fun getAfter(request: ServerRequest): Long {
            return getValue(request, "after")?.toLong() ?: 0
        }

        private fun getCount(request: ServerRequest): Int {
            val count = getValue(request, "count")
            return if (count == null) 10
            else Integer.parseInt(count)
        }

        fun getPagingFromRequest(req: ServerRequest): Paging {
            return Paging(after = getAfter(req), count = getCount(req))
        }
    }
}
