package com.kroffle.knitting.controller.handler.design

import com.kroffle.knitting.controller.handler.design.dto.NewDesign
import com.kroffle.knitting.controller.handler.design.dto.UpdateDesign
import com.kroffle.knitting.controller.handler.design.mapper.DesignRequestMapper
import com.kroffle.knitting.controller.handler.design.mapper.DesignResponseMapper
import com.kroffle.knitting.controller.handler.helper.auth.AuthHelper
import com.kroffle.knitting.controller.handler.helper.exception.ExceptionHelper
import com.kroffle.knitting.controller.handler.helper.extension.ServerRequestExtension.getLongPathVariable
import com.kroffle.knitting.controller.handler.helper.extension.ServerRequestExtension.safetyBodyToMono
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
    fun createDesign(request: ServerRequest): Mono<ServerResponse> {
        val design = request.safetyBodyToMono(NewDesign.Request::class.java)
        val knitterId = AuthHelper.getKnitterId(request)
        return design
            .map { DesignRequestMapper.toCreateDesignData(it, knitterId) }
            .flatMap(service::create)
            .doOnError(ExceptionHelper::raiseException)
            .map(DesignResponseMapper::toNewDesignResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun updateDesign(request: ServerRequest): Mono<ServerResponse> {
        val design = request.safetyBodyToMono(UpdateDesign.Request::class.java)
        val designId = request.getLongPathVariable("designId")
        val knitterId = AuthHelper.getKnitterId(request)
        return design
            .map { DesignRequestMapper.toUpdateDesignData(it, designId, knitterId) }
            .flatMap(service::update)
            .doOnError(ExceptionHelper::raiseException)
            .map(DesignResponseMapper::toUpdateDesignResponse)
            .flatMap(ResponseHelper::makeJsonResponse)
    }

    fun getMyDesigns(request: ServerRequest): Mono<ServerResponse> {
        val paging = PaginationHelper.getPagingFromRequest(request)
        val knitterId = AuthHelper.getKnitterId(request)
        return service
            .getMyDesigns(DesignRequestMapper.toMyDesignFilter(paging, knitterId))
            .doOnError(ExceptionHelper::raiseException)
            .map(DesignResponseMapper::toMyDesignsResponse)
            .collect(toList())
            .flatMap(ResponseHelper::makeJsonResponse)
    }
}
