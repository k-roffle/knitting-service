package com.kroffle.knitting.helper.mocker

import io.mockk.every
import io.mockk.mockkStatic
import java.time.OffsetDateTime
import java.time.ZoneOffset

object TimeMocker {
    private val mockingDate: OffsetDateTime = OffsetDateTime.of(
        2022,
        10,
        10,
        10,
        10,
        10,
        10,
        ZoneOffset.UTC
    )

    fun mockOffsetDateTime() {
        mockkStatic(OffsetDateTime::class)
        every {
            OffsetDateTime.now()
        } returns mockingDate
    }
}
