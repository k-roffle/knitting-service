package com.kroffle.knitting.infra.persistence.design.repository

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.infra.persistence.design.entity.DesignEntity
import com.kroffle.knitting.infra.persistence.design.entity.SizeEntity
import com.kroffle.knitting.infra.persistence.design.entity.TechniqueEntity
import com.kroffle.knitting.infra.persistence.design.entity.toDesignEntity
import com.kroffle.knitting.infra.persistence.design.entity.toSizeEntity
import com.kroffle.knitting.infra.persistence.design.entity.toTechniqueEntities
import com.kroffle.knitting.infra.persistence.helper.pagination.PaginationHelper
import com.kroffle.knitting.usecase.helper.pagination.type.Paging
import com.kroffle.knitting.usecase.helper.pagination.type.Sort
import com.kroffle.knitting.usecase.helper.pagination.type.SortDirection
import com.kroffle.knitting.usecase.repository.DesignRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.stream.Collectors.toList

@Repository
class DesignRepositoryImpl(
    private val designRepository: R2DBCDesignRepository,
    private val techniqueRepository: R2DBCTechniqueRepository,
    private val sizeRepository: R2DBCSizeRepository,
) : DesignRepository {
    override fun createDesign(design: Design): Mono<Design> =
        designRepository
            .save(design.toDesignEntity())
            .flatMap { designEntity ->
                val designId: Long = designEntity.getNotNullId()
                val techniques =
                    techniqueRepository
                        .deleteByDesignId(designId)
                        .flatMap {
                            techniqueRepository
                                .saveAll(design.toTechniqueEntities(designId))
                                .map { it.toTechnique() }
                                .collect(toList())
                        }
                val size =
                    sizeRepository
                        .deleteByDesignId(designId)
                        .flatMap {
                            sizeRepository
                                .save(design.toSizeEntity(designId))
                                .map { it.toSize() }
                        }
                Mono.zip(techniques, size)
                    .map {
                        designEntity.toDesign(techniques = it.t1, size = it.t2)
                    }
            }

    private fun getDesignAggregates(designs: Flux<DesignEntity>): Flux<Design> {
        val designIds: Mono<List<Long>> =
            designs
                .map { design -> design.getNotNullId() }
                .collect(toList())

        val techniqueMap: Mono<Map<Long, Collection<TechniqueEntity>>> =
            designIds
                .flatMap {
                    techniqueRepository
                        .findAllByDesignIdIn(it)
                        .collectMultimap { technique -> technique.getForeignKey() }
                }

        val sizeMap: Mono<Map<Long, SizeEntity>> =
            designIds
                .flatMap {
                    sizeRepository
                        .findAllByDesignIdIn(it)
                        .collectMap { size -> size.getForeignKey() }
                }

        return Mono.zip(techniqueMap, sizeMap)
            .flatMapMany {
                designs
                    .map { design ->
                        val designId = design.getNotNullId()
                        design.toDesign(
                            it.t1[designId]?.map { technique -> technique.toTechnique() } ?: listOf(),
                            it.t2[designId]?.toSize()!!,
                        )
                    }
            }
    }

    override fun getDesignsByKnitterId(knitterId: Long, paging: Paging, sort: Sort): Flux<Design> {
        val pageRequest = PaginationHelper.makePageRequest(paging, sort)

        val designs: Flux<DesignEntity> = when (sort.direction) {
            SortDirection.DESC ->
                if (paging.after != null) {
                    designRepository
                        .findAllByKnitterIdAndIdBefore(
                            knitterId = knitterId,
                            id = paging.after.toLong(),
                            pageable = pageRequest,
                        )
                } else {
                    designRepository
                        .findAllByKnitterId(
                            knitterId = knitterId,
                            pageable = pageRequest,
                        )
                }
            else -> throw NotImplementedError()
        }
        return getDesignAggregates(designs)
    }
}
