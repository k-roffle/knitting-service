package com.kroffle.knitting.infra.configuration.r2dbc.converter

import com.kroffle.knitting.domain.design.enum.DesignType
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter

@WritingConverter
class DesignTypeConverter : Converter<DesignType, DesignType> {
    override fun convert(source: DesignType): DesignType {
        return source
    }
}
