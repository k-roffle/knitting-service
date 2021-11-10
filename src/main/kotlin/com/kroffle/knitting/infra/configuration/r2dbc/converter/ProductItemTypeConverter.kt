package com.kroffle.knitting.infra.configuration.r2dbc.converter

import com.kroffle.knitting.domain.product.value.ProductItem
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter

@WritingConverter
class ProductItemTypeConverter : Converter<ProductItem.Type, ProductItem.Type> {
    override fun convert(source: ProductItem.Type): ProductItem.Type {
        return source
    }
}
