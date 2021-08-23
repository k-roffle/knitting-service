package com.kroffle.knitting.infra.persistence.product.repository

import com.kroffle.knitting.infra.persistence.product.entity.ProductEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface DBProductRepository : ReactiveCrudRepository<ProductEntity, Long>
