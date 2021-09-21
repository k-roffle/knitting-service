package com.kroffle.knitting.controller.handler.design

import com.kroffle.knitting.controller.handler.design.dto.MyDesign
import com.kroffle.knitting.controller.handler.design.dto.NewDesignRequest
import com.kroffle.knitting.controller.handler.design.dto.NewDesignResponse
import com.kroffle.knitting.controller.handler.exception.EmptyBodyException
import com.kroffle.knitting.controller.handler.helper.auth.AuthHelper
import com.kroffle.knitting.controller.handler.helper.pagination.PaginationHelper
import com.kroffle.knitting.controller.handler.helper.response.ResponseHelper
import com.kroffle.knitting.domain.design.value.Gauge
import com.kroffle.knitting.domain.design.value.Length
import com.kroffle.knitting.domain.design.value.Pattern
import com.kroffle.knitting.domain.design.value.Size
import com.kroffle.knitting.domain.design.value.Technique
import com.kroffle.knitting.usecase.design.DesignService
import com.kroffle.knitting.usecase.design.dto.CreateDesignData
import com.kroffle.knitting.usecase.design.dto.MyDesignFilter
import com.kroffle.knitting.usecase.helper.pagination.type.Sort
import com.kroffle.knitting.usecase.helper.pagination.type.SortDirection
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.stream.Collectors.toList

@Component
class DesignHandler(private val service: DesignService) {
    fun createDesign(req: ServerRequest): Mono<ServerResponse> {
        val design: Mono<NewDesignRequest> = req
            .bodyToMono(NewDesignRequest::class.java)
            .switchIfEmpty(Mono.error(EmptyBodyException()))
        val knitterId = AuthHelper.getKnitterId(req)
        return design
            .flatMap {
                service.create(
                    CreateDesignData(
                        knitterId = knitterId,
                        name = it.name,
                        designType = it.designType,
                        patternType = it.patternType,
                        gauge = Gauge(it.stitches, it.rows),
                        size = Size(
                            totalLength = Length(
                                value = it.size.totalLength,
                                unit = it.size.sizeUnit,
                            ),
                            sleeveLength = Length(
                                value = it.size.sleeveLength,
                                unit = it.size.sizeUnit,
                            ),
                            shoulderWidth = Length(
                                value = it.size.shoulderWidth,
                                unit = it.size.sizeUnit,
                            ),
                            bottomWidth = Length(
                                value = it.size.bottomWidth,
                                unit = it.size.sizeUnit,
                            ),
                            armholeDepth = Length(
                                value = it.size.armholeDepth,
                                unit = it.size.sizeUnit,
                            ),
                        ),
                        needle = it.needle,
                        yarn = it.yarn,
                        extra = it.extra,
                        pattern = Pattern(it.pattern),
                        description = it.description,
                        targetLevel = it.targetLevel,
                        coverImageUrl = it.coverImageUrl,
                        techniques = it.techniques.map { technique -> Technique(technique) },
                    )
                )
            }
            .map {
                NewDesignResponse(id = it.id!!)
            }.flatMap {
                ResponseHelper.makeJsonResponse(it)
            }
    }

    fun getMyDesigns(req: ServerRequest): Mono<ServerResponse> {
        val paging = PaginationHelper.getPagingFromRequest(req)
        val knitterId = AuthHelper.getKnitterId(req)
        return service
            .getMyDesign(
                MyDesignFilter(
                    knitterId,
                    paging,
                    Sort("id", SortDirection.DESC),
                )
            )
            .map {
                design ->
                MyDesign(
                    id = design.id!!,
                    name = design.name,
                    yarn = design.yarn,
                    coverImageUrl = design.coverImageUrl,
                    tags = listOf(design.designType.tag, design.patternType.tag),
                    createdAt = design.createdAt!!,
                )
            }
            .collect(toList())
            .flatMap {
                ResponseHelper.makeJsonResponse(it)
            }
    }
}
