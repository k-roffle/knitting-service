package com.kroffle.knitting.infra.design

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface DesignRepository : ReactiveCrudRepository<DesignEntity, Long>
