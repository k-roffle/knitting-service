package com.kroffle.knitting.infra.configuration.r2dbc.converter

import com.kroffle.knitting.domain.design.entity.Design
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter

@WritingConverter
class PatternTypeConverter : Converter<Design.PatternType, Design.PatternType> {
    override fun convert(source: Design.PatternType): Design.PatternType {
        return source
    }
}
