package com.kroffle.knitting.controller.filter.auth

import com.kroffle.knitting.controller.filter.auth.exception.AuthorizationHeaderRequired
import com.kroffle.knitting.controller.handler.helper.exception.ExceptionHelper
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import com.kroffle.knitting.controller.router.auth.CertRouter.Companion.PUBLIC_PATHS as CertRouterPublicPaths
import com.kroffle.knitting.controller.router.auth.LogInRouter.Companion.PUBLIC_PATHS as LogInRouterPublicPaths
import com.kroffle.knitting.controller.router.design.DesignsRouter.Companion.PUBLIC_PATHS as DesignsRouterPublicPaths
import com.kroffle.knitting.controller.router.knitter.MyselfRouter.Companion.PUBLIC_PATHS as MyselfRouterPublicPaths
import com.kroffle.knitting.controller.router.product.ProductsRouter.Companion.PUBLIC_PATHS as ProductsRouterPublicPaths

@Component
class AuthorizationFilter(private val tokenDecoder: TokenDecoder) : WebFilter {
    private fun resolveToken(headers: HttpHeaders): String? {
        val authorizationHeaders = headers[HttpHeaders.AUTHORIZATION]
        if (authorizationHeaders.isNullOrEmpty()) return null
        val bearerToken = authorizationHeaders.first()
        return if (bearerToken.startsWith(HEADER_PREFIX)) {
            bearerToken.substring(HEADER_PREFIX.length)
        } else null
    }

    private fun getAuthorization(headers: HttpHeaders): Mono<Long> {
        val token = resolveToken(headers)
            ?: return Mono.error(AuthorizationHeaderRequired())
        return try {
            Mono.just(tokenDecoder.getKnitterId(token))
        } catch (error: Exception) {
            Mono.error(error)
        }
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val path = exchange.request.path.value()
        if (path in PUBLIC_PATHS) {
            return chain.filter(exchange)
        }
        return getAuthorization(headers = exchange.request.headers)
            .doOnError { error -> ExceptionHelper.raiseException(error) }
            .doOnSuccess { exchange.attributes["knitterId"] = it }
            .then(chain.filter(exchange))
    }

    interface TokenDecoder {
        fun getKnitterId(token: String): Long
    }

    companion object {
        private const val HEADER_PREFIX = "Bearer "
        private val ACTUATOR_PATHS = listOf("/actuator", "/actuator/health")
        private val PUBLIC_PATHS = (
            LogInRouterPublicPaths +
                CertRouterPublicPaths +
                MyselfRouterPublicPaths +
                DesignsRouterPublicPaths +
                ProductsRouterPublicPaths +
                ACTUATOR_PATHS
            )
    }
}
