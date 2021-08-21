package com.kroffle.knitting.infra.persistence.product.repository

import com.kroffle.knitting.infra.persistence.product.entity.ProductItemEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface DBProductItemRepository : ReactiveCrudRepository<ProductItemEntity, Long>
