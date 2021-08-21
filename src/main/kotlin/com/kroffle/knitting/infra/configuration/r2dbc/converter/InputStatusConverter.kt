package com.kroffle.knitting.infra.configuration.r2dbc.converter

import com.kroffle.knitting.domain.product.enum.InputStatus
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter

@WritingConverter
class InputStatusConverter : Converter<InputStatus, InputStatus> {
    override fun convert(source: InputStatus): InputStatus {
        return source
    }
}
