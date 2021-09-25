package com.kroffle.knitting.controller.handler.helper.response

import com.kroffle.knitting.controller.handler.helper.response.type.ListItemPayload
import com.kroffle.knitting.controller.handler.helper.response.type.MetaData
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectPayload
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

object ResponseHelper {
    private class Response<T>(
        val payload: T,
        val meta: MetaData,
    )

    private fun makeKnittingResponse(payload: ObjectPayload): Response<ObjectPayload> =
        Response(
            payload = payload,
            meta = MetaData(),
        )

    private fun makeKnittingResponse(data: List<ListItemPayload>):
        Response<List<ListItemPayload>> {
            val metaData = if (data.isEmpty()) {
                MetaData()
            } else {
                MetaData(lastCursor = data.last().getCursor())
            }
            return Response(data, metaData)
        }

    fun makeJsonResponse(data: ObjectPayload): Mono<ServerResponse> {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(makeKnittingResponse(data))
    }

    fun makeJsonResponse(data: List<ListItemPayload>): Mono<ServerResponse> {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(makeKnittingResponse(data))
    }
}
