package com.kroffle.knitting.infra.design

import com.kroffle.knitting.infra.design.entity.DesignEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface DBDesignRepository : ReactiveCrudRepository<DesignEntity, Long>
