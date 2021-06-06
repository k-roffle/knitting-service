package com.kroffle.knitting.controller.router.auth

import com.kroffle.knitting.controller.handler.auth.GoogleLogInHandler
import com.kroffle.knitting.infra.jwt.TokenHelper
import com.kroffle.knitting.infra.oauth.GoogleOauthHelperImpl
import com.kroffle.knitting.infra.properties.SelfProperties
import com.kroffle.knitting.usecase.auth.AuthService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import java.time.LocalDateTime

@WebFluxTest
@ExtendWith(SpringExtension::class)
class LoginRouterTest {
    lateinit var webClient: WebTestClient

    lateinit var selfProperties: SelfProperties

    lateinit var tokenHelper: TokenHelper

    @BeforeEach
    fun setUp() {
        selfProperties = SelfProperties()
        selfProperties.host = "localhost:2028"
        selfProperties.env = "test"

        tokenHelper = TokenHelper("I'M SECRET KEY!")

        val routerFunction = LogInRouter(
            GoogleLogInHandler(
                AuthService(
                    GoogleOauthHelperImpl(
                        selfProperties,
                        "GOOGLE_CLIENT_ID"
                    ),
                    tokenHelper,
                )
            )
        ).logInRouterFunction()
        webClient = WebTestClient.bindToRouterFunction(routerFunction).build()
    }

    @Test
    fun `로그인 요청 시 구글 인증 페이지로 리다이렉트 되어야 함`() {
        val expectedLocation = "https://accounts.google.com/o/oauth2/v2/auth" +
            "?scope=profile" +
            "&access_type=offline" +
            "&include_granted_scopes=true" +
            "&response_type=code" +
            "&redirect_uri=https://localhost:2028/auth/google/authorized" +
            "&client_id=GOOGLE_CLIENT_ID"

        webClient
            .post()
            .uri("/auth/google/code")
            .exchange()
            .expectStatus().isTemporaryRedirect
            .expectHeader()
            .location(expectedLocation)
    }

    @Test
    fun `구글 인증 후 access token 을 발급 받을 수 있어야 함`() {
        val result = webClient
            .get()
            .uri("/auth/google/authorized")
            .exchange()
            .expectStatus().isOk
            .expectBody<String>()
            .returnResult()
            .responseBody!!
        val regex = Regex("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")
        assert(regex.matchEntire(tokenHelper.getAuthorizedUserId(result).toString()) != null)
        val expiration = tokenHelper.getAuthorizationExp(result)!!
        assert(expiration > LocalDateTime.now())
    }
}
