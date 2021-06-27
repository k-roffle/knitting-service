package com.kroffle.knitting.infra.configuration

import com.kroffle.knitting.domain.design.enum.DesignType
import com.kroffle.knitting.domain.design.enum.PatternType
import com.kroffle.knitting.infra.properties.DatabaseProperties
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.postgresql.codec.EnumCodec
import io.r2dbc.spi.ConnectionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@WritingConverter
class DesignTypeConverter : Converter<DesignType, DesignType> {
    override fun convert(source: DesignType): DesignType {
        return source
    }
}

@WritingConverter
class PatternTypeConverter : Converter<PatternType, PatternType> {
    override fun convert(source: PatternType): PatternType {
        return source
    }
}

@Configuration
@EnableR2dbcRepositories
class R2dbcConfiguration : AbstractR2dbcConfiguration() {
    @Autowired
    lateinit var appProperties: DatabaseProperties

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

    override fun getCustomConverters(): MutableList<Any> {
        return mutableListOf(DesignTypeConverter(), PatternTypeConverter())
    }

    companion object {
        private const val DESIGN_TYPE = "design_type"
        private const val PATTERN_TYPE = "pattern_type"
    }
}
