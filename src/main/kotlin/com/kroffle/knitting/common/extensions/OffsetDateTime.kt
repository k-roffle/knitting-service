package com.kroffle.knitting.common.extensions

import java.time.OffsetDateTime
import java.time.ZoneOffset

fun OffsetDateTime.onUTC() = this.withOffsetSameInstant(ZoneOffset.UTC)
