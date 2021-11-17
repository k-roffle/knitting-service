package com.kroffle.knitting.controller.filter.cors

import com.kroffle.knitting.infra.properties.WebApplicationProperties
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.cors.reactive.CorsUtils
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class CORSFilter(private val webProperties: WebApplicationProperties) : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request = exchange.request
        if (CorsUtils.isCorsRequest(request)) {
            val response = exchange.response
            val headers = response.headers

            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, webProperties.origins.joinToString(","))
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "*")
            headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600")
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Authorization")
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type")

            if (request.method == HttpMethod.OPTIONS) {
                response.statusCode = HttpStatus.OK
                return Mono.empty()
            }
        }
        return chain.filter(exchange)
    }
}
