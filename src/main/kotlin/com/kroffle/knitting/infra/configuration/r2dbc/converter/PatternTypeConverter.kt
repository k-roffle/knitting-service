package com.kroffle.knitting.infra.configuration.r2dbc.converter

import com.kroffle.knitting.domain.design.enum.PatternType
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter

@WritingConverter
class PatternTypeConverter : Converter<PatternType, PatternType> {
    override fun convert(source: PatternType): PatternType {
        return source
    }
}
