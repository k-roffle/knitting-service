package com.kroffle.knitting.controller.handler.helper.pagination

import com.kroffle.knitting.controller.handler.helper.pagination.exception.InvalidPagingParameter
import com.kroffle.knitting.usecase.helper.pagination.type.Paging
import org.springframework.web.reactive.function.server.ServerRequest

class PaginationHelper {
    companion object {
        private fun getValue(request: ServerRequest, key: String): String? {
            val value = request.queryParam(key)
            return if (value.isEmpty) null
            else value.get()
        }

        private fun getAfter(request: ServerRequest): String? {
            return getValue(request, "after")
        }

        private fun getCount(request: ServerRequest): Int {
            val count = getValue(request, "count")
            return if (count == null) 10
            else Integer.parseInt(count)
        }

        fun getPagingFromRequest(req: ServerRequest): Paging {
            try {
                return Paging(after = getAfter(req), count = getCount(req))
            } catch (e: IllegalArgumentException) {
                throw InvalidPagingParameter()
            }
        }
    }
}
