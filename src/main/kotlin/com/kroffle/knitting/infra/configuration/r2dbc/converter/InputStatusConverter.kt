package com.kroffle.knitting.infra.configuration.r2dbc.converter

import com.kroffle.knitting.domain.product.entity.Product
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter

@WritingConverter
class InputStatusConverter : Converter<Product.InputStatus, Product.InputStatus> {
    override fun convert(source: Product.InputStatus): Product.InputStatus {
        return source
    }
}
