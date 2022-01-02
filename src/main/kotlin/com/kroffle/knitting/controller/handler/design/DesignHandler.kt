package com.kroffle.knitting.controller.handler.design

import com.kroffle.knitting.controller.handler.design.dto.NewDesign
import com.kroffle.knitting.controller.handler.design.mapper.DesignRequestMapper
import com.kroffle.knitting.controller.handler.design.mapper.DesignResponseMapper
import com.kroffle.knitting.controller.handler.exception.EmptyBodyException
import com.kroffle.knitting.controller.handler.exception.InvalidBodyException
import com.kroffle.knitting.controller.handler.helper.auth.AuthHelper
import com.kroffle.knitting.controller.handler.helper.exception.ExceptionHelper
import com.kroffle.knitting.controller.handler.helper.pagination.PaginationHelper
import com.kroffle.knitting.controller.handler.helper.response.ResponseHelper
import com.kroffle.knitting.usecase.design.DesignService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.stream.Collectors.toList

@Component
class DesignHandler(private val service: DesignService) {
    fun createDesign(req: ServerRequest): Mono<ServerResponse> {
        val design: Mono<NewDesign.Request> = req
            .bodyToMono(NewDesign.Request::class.java)
            .onErrorResume { Mono.error(InvalidBodyException()) }
            .switchIfEmpty(Mono.error(EmptyBodyException()))
        val knitterId = AuthHelper.getKnitterId(req)
        return design
            .map { DesignRequestMapper.toCreateDesignData(it, knitterId) }
            .flatMap(service::create)
            .doOnError(ExceptionHelper::raiseException)
            .map(DesignResponseMapper::toNewDesignResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun getMyDesigns(req: ServerRequest): Mono<ServerResponse> {
        val paging = PaginationHelper.getPagingFromRequest(req)
        val knitterId = AuthHelper.getKnitterId(req)
        return service
            .getMyDesign(DesignRequestMapper.toMyDesignFilter(paging, knitterId))
            .doOnError(ExceptionHelper::raiseException)
            .map(DesignResponseMapper::toMyDesignResponse)
            .collect(toList())
            .flatMap(ResponseHelper::makeJsonResponse)
    }
}
