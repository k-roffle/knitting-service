package com.kroffle.knitting.usecase.summary

import com.kroffle.knitting.domain.product.entity.Product
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ProductSummaryService(private val repository: ProductRepository) {
    fun countProductOnList(knitterId: Long): Mono<Long> =
        repository
            .findRegisteredProduct(knitterId)
            .filter { it.onList }
            .count()

    interface ProductRepository {
        fun findRegisteredProduct(knitterId: Long): Flux<Product>
    }
}
