package com.kroffle.knitting.domain.helper

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlin.reflect.KProperty1

object DraftValueReader {
    val objectMapper = ObjectMapper()

    abstract class TruncatedValue(
        @JsonAnySetter
        @get:JsonAnyGetter
        val truncated: MutableMap<String, Any> = sortedMapOf(),
    )

    inline fun <reified T : TruncatedValue, R> read(value: String, getter: KProperty1<T, R>): R =
        getter(this.objectMapper.readValue(value))
}
