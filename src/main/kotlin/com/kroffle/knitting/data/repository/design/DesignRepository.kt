package com.kroffle.knitting.data.repository.design

import com.kroffle.knitting.data.entity.design.DesignEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface DesignRepository : ReactiveCrudRepository<DesignEntity, Long>
