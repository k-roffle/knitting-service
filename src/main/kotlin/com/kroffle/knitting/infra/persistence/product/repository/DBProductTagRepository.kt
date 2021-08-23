package com.kroffle.knitting.infra.persistence.product.repository

import com.kroffle.knitting.infra.persistence.product.entity.ProductTagEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface DBProductTagRepository : ReactiveCrudRepository<ProductTagEntity, Long>
