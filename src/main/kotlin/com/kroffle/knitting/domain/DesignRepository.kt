package com.kroffle.knitting.domain

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface DesignRepository : ReactiveCrudRepository<Design, Long>
