package com.kroffle.knitting.controller.router.auth

import com.kroffle.knitting.controller.handler.auth.GoogleLogInHandler
import com.kroffle.knitting.controller.handler.auth.dto.Authorized
import com.kroffle.knitting.controller.handler.auth.dto.RefreshToken
import com.kroffle.knitting.domain.knitter.entity.Knitter
import com.kroffle.knitting.helper.TestResponse
import com.kroffle.knitting.helper.WebTestClientHelper
import com.kroffle.knitting.helper.extension.addDefaultRequestHeader
import com.kroffle.knitting.infra.jwt.TokenDecoder
import com.kroffle.knitting.infra.jwt.TokenPublisher
import com.kroffle.knitting.infra.oauth.GoogleOAuthHelperImpl
import com.kroffle.knitting.infra.oauth.dto.ClientInfo
import com.kroffle.knitting.infra.oauth.dto.GoogleOAuthConfig
import com.kroffle.knitting.infra.persistence.knitter.entity.KnitterEntity
import com.kroffle.knitting.infra.properties.WebApplicationProperties
import com.kroffle.knitting.usecase.auth.AuthService
import com.kroffle.knitting.usecase.auth.dto.OAuthProfile
import com.kroffle.knitting.usecase.repository.KnitterRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.verify
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Mono
import java.time.OffsetDateTime

@WebFluxTest
@ExtendWith(SpringExtension::class)
class LoginRouterTest {
    private lateinit var webClient: WebTestClient

    private lateinit var webClientWithMockOAuthHelper: WebTestClient

    @MockBean
    private lateinit var mockOAuthHelper: AuthService.OAuthHelper

    @MockBean
    private lateinit var tokenDecoder: TokenDecoder

    @MockBean
    private lateinit var tokenPublisher: TokenPublisher

    @MockBean
    private lateinit var repository: KnitterRepository

    @MockBean
    private lateinit var webProperties: WebApplicationProperties

    @BeforeEach
    fun setUp() {
        val oAuthHelper = GoogleOAuthHelperImpl(
            ClientInfo(
                "http",
                "localhost:2028"
            ),
            GoogleOAuthConfig(
                "GOOGLE_CLIENT_ID",
                "GOOGLE_SECRET_KEY",
            ),
        )
        tokenPublisher = TokenPublisher(WebTestClientHelper.JWT_SECRET_KEY)
        tokenDecoder = TokenDecoder(WebTestClientHelper.JWT_SECRET_KEY)

        webClient = WebTestClientHelper
            .createWebTestClient(
                LogInRouter(
                    GoogleLogInHandler(
                        AuthService(oAuthHelper, tokenPublisher, repository),
                    )
                ).logInRouterFunction()
            )

        webClientWithMockOAuthHelper = WebTestClientHelper
            .createWebTestClient(
                LogInRouter(
                    GoogleLogInHandler(
                        AuthService(mockOAuthHelper, tokenPublisher, repository),
                    )
                ).logInRouterFunction()
            )
    }

    @Test
    fun `로그인 요청 시 구글 인증 페이지로 리다이렉트 되어야 함`() {
        val expectedLocation = "https://accounts.google.com/o/oauth2/v2/auth" +
            "?scope=profile+https://www.googleapis.com/auth/userinfo.email" +
            "&access_type=offline" +
            "&include_granted_scopes=true" +
            "&response_type=code" +
            "&redirect_uri=http://localhost:2028/login/redirected" +
            "&client_id=GOOGLE_CLIENT_ID"

        webClient
            .get()
            .uri("/auth/google/code")
            .exchange()
            .expectStatus().isTemporaryRedirect
            .expectHeader()
            .location(expectedLocation)
    }

    @Test
    fun `이미 가입한 유저인 경우 access token 을 발급 받을 수 있어야 함`() {
        val targetKnitter = KnitterEntity(
            id = 1,
            email = "mock@email.com",
            name = null,
            profileImageUrl = null,
            createdAt = OffsetDateTime.now(),
        ).toKnitter()

        given(mockOAuthHelper.getProfile("MOCK_CODE")).willReturn(
            Mono.just(
                OAuthProfile(
                    email = targetKnitter.email,
                    name = "John Doe",
                    profileImageUrl = null
                )
            )
        )

        given(repository.findByEmail(targetKnitter.email)).willReturn(
            Mono.just(targetKnitter)
        )

        val result = webClientWithMockOAuthHelper
            .get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/auth/google/authorized")
                    .queryParam("code", "MOCK_CODE")
                    .build()
            }
            .exchange()
            .expectStatus().isOk
            .expectBody<TestResponse<Authorized.Response>>()
            .returnResult()
            .responseBody!!
        assert(tokenDecoder.getKnitterId(result.payload.token) == targetKnitter.id)
    }

    @Test
    fun `새로 가입하는 유저의 경우 계정을 생성한 후 access token 을 발급 받을 수 있어야 함`() {
        val newKnitterId: Long = 1
        val newUserCreatedAt = OffsetDateTime.now()
        val mockUser = Knitter(
            id = newKnitterId,
            email = "new@email.com",
            name = "Jessica Mars",
            profileImageUrl = "https://image.com",
            createdAt = newUserCreatedAt,
        )

        given(mockOAuthHelper.getProfile("MOCK_CODE")).willReturn(
            Mono.just(
                OAuthProfile(
                    email = mockUser.email,
                    name = mockUser.name!!,
                    profileImageUrl = mockUser.profileImageUrl,
                )
            )
        )

        given(repository.findByEmail(mockUser.email))
            .willReturn(Mono.empty())

        given(repository.create(any()))
            .willReturn(Mono.just(mockUser))

        val result = webClientWithMockOAuthHelper
            .get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/auth/google/authorized")
                    .queryParam("code", "MOCK_CODE")
                    .build()
            }
            .exchange()
            .expectStatus().isOk
            .expectBody<TestResponse<Authorized.Response>>()
            .returnResult()
            .responseBody!!

        assert(tokenDecoder.getKnitterId(result.payload.token) == newKnitterId)

        verify(repository).create(
            argThat { param ->
                assert(param.id == null)
                assert(param.email == "new@email.com")
                assert(param.name == "Jessica Mars")
                assert(param.profileImageUrl == "https://image.com")
                assert(param.createdAt == null)
                true
            }
        )
    }

    @Test
    fun `리프레시 요청시 동일한 유저 id로 토큰이 갱신 되어야 함`() {
        val result = webClient
            .post()
            .uri("/auth/refresh")
            .addDefaultRequestHeader()
            .exchange()
            .expectStatus().isOk
            .expectBody<TestResponse<RefreshToken.Response>>()
            .returnResult()
            .responseBody!!
        val decodedToken = TokenDecoder(WebTestClientHelper.JWT_SECRET_KEY)
            .getKnitterId(result.payload.token)
        assert(decodedToken == WebTestClientHelper.AUTHORIZED_KNITTER_ID)
    }
}
