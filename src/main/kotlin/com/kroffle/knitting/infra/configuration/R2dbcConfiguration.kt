package com.kroffle.knitting.infra.configuration

import com.kroffle.knitting.domain.design.entity.Design
import com.kroffle.knitting.domain.product.entity.Product
import com.kroffle.knitting.domain.product.value.ProductItem
import com.kroffle.knitting.infra.configuration.r2dbc.converter.DesignTypeConverter
import com.kroffle.knitting.infra.configuration.r2dbc.converter.InputStatusConverter
import com.kroffle.knitting.infra.configuration.r2dbc.converter.PatternTypeConverter
import com.kroffle.knitting.infra.configuration.r2dbc.converter.ProductItemTypeConverter
import com.kroffle.knitting.infra.properties.DatabaseProperties
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.postgresql.codec.EnumCodec
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@EnableR2dbcRepositories
class R2dbcConfiguration(private val databaseProperties: DatabaseProperties) : AbstractR2dbcConfiguration() {
    @Bean
    override fun connectionFactory(): ConnectionFactory = PostgresqlConnectionFactory(
        PostgresqlConnectionConfiguration.builder()
            .host(databaseProperties.host)
            .username(databaseProperties.username)
            .password(databaseProperties.password)
            .database(databaseProperties.database)
            .codecRegistrar(EnumCodec.builder().withEnum(DESIGN_TYPE, Design.DesignType::class.java).build())
            .codecRegistrar(EnumCodec.builder().withEnum(PATTERN_TYPE, Design.PatternType::class.java).build())
            .codecRegistrar(EnumCodec.builder().withEnum(INPUT_STATUS, Product.InputStatus::class.java).build())
            .codecRegistrar(EnumCodec.builder().withEnum(PRODUCT_ITEM_TYPE, ProductItem.Type::class.java).build())
            .build()
    )

    override fun getCustomConverters(): MutableList<Any> {
        return mutableListOf(
            DesignTypeConverter(),
            PatternTypeConverter(),
            InputStatusConverter(),
            ProductItemTypeConverter(),
        )
    }

    companion object {
        private const val DESIGN_TYPE = "design_type"
        private const val PATTERN_TYPE = "pattern_type"
        private const val INPUT_STATUS = "input_status"
        private const val PRODUCT_ITEM_TYPE = "product_item_type"
    }
}
