package com.kroffle.knitting.infra.oauth

import com.kroffle.knitting.infra.properties.SelfProperties
import com.kroffle.knitting.infra.properties.WebApplicationProperties
import com.kroffle.knitting.usecase.auth.GoogleLogInHandler
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.server.ServerWebInputException
import org.springframework.web.util.UriComponentsBuilder
import reactor.kotlin.core.publisher.toMono
import reactor.core.publisher.Mono
import reactor.core.publisher.Signal.isError


class Request(
    private val code: String,
    private val clientId: String,
    private val clientSecret: String,
    private val redirectUri: String,
    private val grantType: String = "authorization_code",
)

class Response(
    private val accessToken: String,
    private val expiresIn: Int,
    private val refreshToken: String,
    private val scope: String,
    private val tokenType: String,
    private val idToken: String
) {
    fun getAccessToken(): String = this.accessToken

    fun verifyAccessToken(): Boolean {
        print(this.accessToken)
        return true
    }
}

class GoogleOauthHelperImpl(
    private val selfProperties: SelfProperties,
    private val webProperties: WebApplicationProperties
) : GoogleLogInHandler.GoogleOAuthHelper {

    private fun getCallbackUri(): String {
        val scheme = when (selfProperties.env) {
            "local" -> "http"
            else -> "https"
        }
        return UriComponentsBuilder.newInstance()
            .scheme(scheme)
            .host(selfProperties.host)
            .path("/auth/google/authorized")
            .build()
            .toUriString()
    }

    override fun getAuthorizationUri(): String =
        UriComponentsBuilder.newInstance()
            .scheme("https")
            .host("accounts.google.com")
            .path("/o/oauth2/v2/auth")
            .queryParam("scope", "profile")
            .queryParam("access_type", "offline")
            .queryParam("include_granted_scopes", "true")
            .queryParam("response_type", "code")
            .queryParam("redirect_uri", getCallbackUri())
            .queryParam("client_id", webProperties.googleClientId)
            .build()
            .toUriString()

    override fun getGoogleAccessToken(code: String): Mono<String> {
        val webClient = WebClient.create("https://oauth2.googleapis.com")
        val aa = Request(
            code,
            webProperties.googleClientId,
            webProperties.googleClientSecret,
            getCallbackUri(),
        )
        val body = BodyInserters.fromValue(aa)
        val responseMono = webClient
            .post()
            .uri("/token")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .retrieve()
            .onStatus(HttpStatus::isError) {
                println(it.statusCode())
                println(it.bodyToMono(String::class.java))
                println(it.)
                println("???????????????????")
                Mono.error(Exception("?????? whywhy"))
            }
            .bodyToMono(Response::class.java)
        return responseMono
            .flatMap { Mono.just(it.verifyAccessToken()) }
            .flatMap {
                if (it) {
                    responseMono.flatMap { Mono.just(it.getAccessToken()) }
                } else {
                    Mono.error(Exception("qwerty"))
                }
            }
    }
//                    .flatMap( clientResponse -> {
//                //Error handling
//                if ( clientResponse.statusCode().isError() ) { // or clientResponse.statusCode().value() >= 400
//                    return clientResponse.createException().flatMap( Mono::error );
//                }
//                return clientResponse.bodyToMono( clazz )
//            } )
//
//                .retrieve()
//                .bodyToMono(Response::class.java)
//                .doOnError {
//                    print("HI")
//                    print(it.)
//                }
//                .subscribe {
//                    print(it.accessToken)
//                }

}
