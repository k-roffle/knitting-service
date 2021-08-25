package com.kroffle.knitting.infra.configuration.r2dbc.converter

import com.kroffle.knitting.domain.product.enum.ProductItemType
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter

@WritingConverter
class ProductItemTypeConverter : Converter<ProductItemType, ProductItemType> {
    override fun convert(source: ProductItemType): ProductItemType {
        return source
    }
}
