package com.kroffle.knitting.controller.filter.cors

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
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
class CORSFilter : WebFilter {
//    TODO origin 변경
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request = exchange.request
        if (CorsUtils.isCorsRequest(request)) {
            val response = exchange.response
            val headers = response.headers
            headers.add("Access-Control-Allow-Origin", "*")
            headers.add("Access-Control-Allow-Methods", "*")
            headers.add("Access-Control-Max-Age", "3600")
            headers.add("Access-Control-Allow-Headers", "my-token")
            headers.add("Access-Control-Allow-Headers", "Content-Type")
            if (request.method == HttpMethod.OPTIONS) {
                response.statusCode = HttpStatus.OK
                return Mono.empty()
            }
        }
        return chain.filter(exchange)
    }
}
