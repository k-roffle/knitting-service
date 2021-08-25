package com.kroffle.knitting.helper.extension

import com.kroffle.knitting.helper.WebTestClientHelper
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

fun WebTestClient.RequestHeadersSpec<*>.addDefaultRequestHeader(
    authorized: Boolean = true,
    mediaType: MediaType = MediaType.APPLICATION_JSON,
): WebTestClient.RequestHeadersSpec<*> =
    WebTestClientHelper.addDefaultRequestHeader(this, authorized, mediaType)
