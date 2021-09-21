package com.kroffle.knitting.infra.persistence.design.repository

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.infra.persistence.design.entity.DesignEntity
import com.kroffle.knitting.infra.persistence.design.entity.TechniqueEntity
import com.kroffle.knitting.infra.persistence.design.entity.toDesignEntity
import com.kroffle.knitting.infra.persistence.design.entity.toTechniqueEntities
import com.kroffle.knitting.infra.persistence.helper.pagination.PaginationHelper
import com.kroffle.knitting.usecase.helper.pagination.type.Paging
import com.kroffle.knitting.usecase.helper.pagination.type.Sort
import com.kroffle.knitting.usecase.helper.pagination.type.SortDirection
import com.kroffle.knitting.usecase.repository.DesignRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.stream.Collectors.toList

@Component
class R2dbcDesignRepository(
    private val designRepository: DBDesignRepository,
    private val techniqueRepository: DBTechniqueRepository,
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
                techniques.map {
                    designEntity.toDesign(techniques = it)
                }
            }

    private fun getDesignAggregate(designs: Flux<DesignEntity>): Flux<Design> {
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

        return designs
            .concatMap { design ->
                techniqueMap
                    .map { techniques ->
                        val designId = design.getNotNullId()
                        design
                            .toDesign(
                                techniques[designId]
                                    ?.map { technique -> technique.toTechnique() }
                                    ?: listOf()
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
        return getDesignAggregate(designs)
    }
}
