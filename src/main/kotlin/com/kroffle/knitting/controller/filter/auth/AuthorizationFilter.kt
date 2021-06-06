package com.kroffle.knitting.controller.filter.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID
import com.kroffle.knitting.controller.router.auth.LogInRouter.Companion.PUBLIC_PATHS as LogInRouterPublicPaths
import com.kroffle.knitting.controller.router.design.DesignRouter.Companion.PUBLIC_PATHS as DesignRouterPublicPaths
import com.kroffle.knitting.controller.router.design.DesignsRouter.Companion.PUBLIC_PATHS as DesignsRouterPublicPaths
import com.kroffle.knitting.controller.router.ping.PingRouter.Companion.PUBLIC_PATHS as PingRouterPublicPaths

@Component
class AuthorizationFilter : WebFilter {
    @Autowired
    lateinit var tokenHelper: TokenHelper

    private fun resolveToken(headers: HttpHeaders): String? {
        val authorizationHeaders = headers[HttpHeaders.AUTHORIZATION]
        if (authorizationHeaders.isNullOrEmpty()) return null
        val bearerToken = authorizationHeaders.first()
        return if (bearerToken.startsWith(HEADER_PREFIX)) {
            bearerToken.substring(HEADER_PREFIX.length)
        } else null
    }

    private fun getAuthorization(headers: HttpHeaders): Mono<UUID> {
        val token = resolveToken(headers)
            ?: return Mono.error(ResponseStatusException(HttpStatus.UNAUTHORIZED, "Header is Empty"))
        if (tokenHelper.validateToken(token)) {
            return Mono.just(tokenHelper.getAuthorizedUserId(token)!!)
        }
        throw Error("invalid token")
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val path = exchange.request.path.value()
        if (path in PUBLIC_PATHS) {
            return chain.filter(exchange)
        }
        return getAuthorization(headers = exchange.request.headers).doOnError {
            error ->
            exchange.response.statusCode = HttpStatus.UNAUTHORIZED
            val message = error.message
            if (message != null) {
                val byteMessages = message.toByteArray()
                val buffer = exchange.response.bufferFactory().wrap(byteMessages)
                exchange.response.writeWith(Flux.just(buffer))
            }
        }.then(
            chain.filter(exchange)
        )
    }

    interface TokenHelper {
        fun getAuthorizedUserId(token: String): UUID?
        fun validateToken(token: String): Boolean
    }

    companion object {
        private const val HEADER_PREFIX = "Bearer "
        private val PUBLIC_PATHS = (
            LogInRouterPublicPaths +
                DesignRouterPublicPaths +
                DesignsRouterPublicPaths +
                PingRouterPublicPaths
            )
    }
}
