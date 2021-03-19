package com.kroffle.knitting.config

import com.kroffle.knitting.AppProperties
import com.kroffle.knitting.domain.DesignType
import com.kroffle.knitting.domain.PatternType
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.postgresql.codec.EnumCodec
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.beans.factory.annotation.Autowired

@Configuration
@EnableR2dbcRepositories
class R2dbcConfiguration: AbstractR2dbcConfiguration() {
    @Autowired
    lateinit var appProperties: AppProperties

    companion object {
        private const val DESIGN_TYPE = "design_type"
        private const val PATTERN_TYPE = "pattern_type"
    }

    @Bean
    override fun connectionFactory(): ConnectionFactory = PostgresqlConnectionFactory(
        PostgresqlConnectionConfiguration.builder()
            .host(appProperties.host)
            .username(appProperties.username)
            .password(appProperties.password)
            .database(appProperties.database)
            .codecRegistrar(EnumCodec.builder().withEnum(DESIGN_TYPE, DesignType::class.java).build())
            .codecRegistrar(EnumCodec.builder().withEnum(PATTERN_TYPE, PatternType::class.java).build())
            .build()
    )
}
