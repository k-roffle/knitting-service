package com.kroffle.knitting.controller.router.auth

import com.kroffle.knitting.controller.filter.auth.AuthorizationFilter
import com.kroffle.knitting.controller.handler.auth.GoogleLogInHandler
import com.kroffle.knitting.controller.handler.auth.model.AuthorizedResponse
import com.kroffle.knitting.controller.handler.auth.model.RefreshTokenResponse
import com.kroffle.knitting.infra.jwt.TokenDecoder
import com.kroffle.knitting.infra.jwt.TokenPublisher
import com.kroffle.knitting.infra.knitter.entity.KnitterEntity
import com.kroffle.knitting.infra.oauth.GoogleOauthHelperImpl
import com.kroffle.knitting.infra.properties.SelfProperties
import com.kroffle.knitting.usecase.auth.AuthService
import com.kroffle.knitting.usecase.auth.KnitterRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Mono
import java.util.UUID

@WebFluxTest
@ExtendWith(SpringExtension::class)
class LoginRouterTest {
    private lateinit var webClient: WebTestClient

    private lateinit var selfProperties: SelfProperties

    private lateinit var tokenPublisher: TokenPublisher

    @MockBean
    private lateinit var tokenDecoder: TokenDecoder

    @MockBean
    lateinit var repo: KnitterRepository

    private val secretKey = "I'M SECRET KEY!"

    @BeforeEach
    fun setUp() {
        selfProperties = SelfProperties()
        selfProperties.host = "localhost:2028"
        selfProperties.env = "test"

        tokenPublisher = TokenPublisher(secretKey)
        tokenDecoder = TokenDecoder(secretKey)

        val routerFunction = LogInRouter(
            GoogleLogInHandler(
                AuthService(
                    GoogleOauthHelperImpl(
                        selfProperties,
                        "GOOGLE_CLIENT_ID",
                        "GOOGLE_SECRET_KEY",
                    ),
                    tokenPublisher,
                    repo,
                ),
            )
        ).logInRouterFunction()
        webClient = WebTestClient
            .bindToRouterFunction(routerFunction)
            .webFilter<WebTestClient.RouterFunctionSpec>(AuthorizationFilter(tokenDecoder))
            .build()
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
        given(repo.findByEmail("devuri404@gmail.com")).willReturn(
            Mono.just(
                KnitterEntity(
                    id = UUID.randomUUID(),
                    email = "devuri404@gmail.com",
                    name = null,
                    profileImageUrl = null,
                ).toKnitter(),
            )
        )

        val result = webClient
            .get()
            .uri {
                uriBuilder ->
                uriBuilder
                    .path("/auth/google/authorized")
                    .queryParam("code", "MOCK_CODE")
                    .build()
            }
            .exchange()
            .expectStatus().isOk
            .expectBody<AuthorizedResponse>()
            .returnResult()
            .responseBody!!
        tokenDecoder.getAuthorizedUserId(result.token)
    }

    @Test
    fun `리프레시 요청시 동일한 유저 id로 토큰이 갱신 되어야 함`() {
        val userId = UUID.randomUUID()
        val token = tokenPublisher.publish(userId)
        val result = webClient
            .post()
            .uri("/auth/refresh")
            .header("Authorization", "Bearer $token")
            .exchange()
            .expectStatus().isOk
            .expectBody<RefreshTokenResponse>()
            .returnResult()
            .responseBody!!
        assert(TokenDecoder(secretKey).getAuthorizedUserId(result.token) == userId)
    }
}
