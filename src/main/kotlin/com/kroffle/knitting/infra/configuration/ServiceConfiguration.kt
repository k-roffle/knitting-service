package com.kroffle.knitting.infra.configuration

import com.kroffle.knitting.usecase.design.DesignService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ServiceConfiguration {
    @Bean
    fun designService(repository: DesignService.DesignRepository) = DesignService(repository)
}
