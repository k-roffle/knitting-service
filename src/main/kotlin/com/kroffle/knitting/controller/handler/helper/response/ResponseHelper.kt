package com.kroffle.knitting.controller.handler.helper.response

import com.kroffle.knitting.controller.handler.helper.response.type.APIResponse
import com.kroffle.knitting.controller.handler.helper.response.type.ListItemData
import com.kroffle.knitting.controller.handler.helper.response.type.ListMetaData
import com.kroffle.knitting.controller.handler.helper.response.type.MetaData
import com.kroffle.knitting.controller.handler.helper.response.type.ObjectData
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class ResponseHelper {
    companion object {
        private fun makeKnittingResponse(data: ObjectData): APIResponse<ObjectData> =
            APIResponse(
                data = data,
                meta = MetaData(),
            )

        private fun makeKnittingResponse(data: List<ListItemData>):
            APIResponse<List<ListItemData>> =
                APIResponse(
                    data = data,
                    meta = ListMetaData(
                        lastCursor = data.last().getCursor(),
                    )
                )

        fun makeJsonResponse(data: ObjectData): Mono<ServerResponse> {
            return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(makeKnittingResponse(data))
        }

        fun makeJsonResponse(data: List<ListItemData>): Mono<ServerResponse> {
            return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(makeKnittingResponse(data))
        }
    }
}
