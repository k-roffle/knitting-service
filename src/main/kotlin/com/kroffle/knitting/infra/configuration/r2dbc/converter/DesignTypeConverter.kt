package com.kroffle.knitting.infra.configuration.r2dbc.converter

import com.kroffle.knitting.domain.design.entity.Design
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter

@WritingConverter
class DesignTypeConverter : Converter<Design.DesignType, Design.DesignType> {
    override fun convert(source: Design.DesignType): Design.DesignType {
        return source
    }
}
