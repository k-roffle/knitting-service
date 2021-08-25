package com.kroffle.knitting.controller.router.auth

import com.kroffle.knitting.controller.handler.auth.ProfileHandler
import com.kroffle.knitting.controller.handler.auth.dto.MyProfileResponse
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
import com.kroffle.knitting.usecase.auth.KnitterRepository
import org.assertj.core.api.Assertions.assertThat
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

@WebFluxTest
@ExtendWith(SpringExtension::class)
class ProfileRouterTest {
    private lateinit var webClient: WebTestClient

    @MockBean
    private lateinit var repository: KnitterRepository

    @MockBean
    private lateinit var mockOAuthHelper: AuthService.OAuthHelper

    @MockBean
    private lateinit var tokenDecoder: TokenDecoder

    @MockBean
    private lateinit var tokenPublisher: TokenPublisher

    @MockBean
    private lateinit var webProperties: WebApplicationProperties

    @BeforeEach
    fun setUp() {
        webClient = WebTestClientHelper
            .createWebTestClient(
                ProfileRouter(
                    ProfileHandler(
                        AuthService(
                            GoogleOAuthHelperImpl(
                                ClientInfo(
                                    "http",
                                    "localhost:2028"
                                ),
                                GoogleOAuthConfig(
                                    "GOOGLE_CLIENT_ID",
                                    "GOOGLE_SECRET_KEY",
                                ),
                            ),
                            tokenPublisher,
                            repository,
                        ),
                    )
                ).profileRouterFunction()
            )
    }

    @Test
    fun `내 프로필을 조회할 수 있어야 함`() {
        given(repository.findById(WebTestClientHelper.AUTHORIZED_KNITTER_ID))
            .willReturn(
                Mono.just(
                    KnitterEntity(
                        id = WebTestClientHelper.AUTHORIZED_KNITTER_ID,
                        name = "홍길동",
                        email = "test@test.com",
                        profileImageUrl = null,
                    ).toKnitter()
                )
            )

        val result = webClient
            .get()
            .uri("/profile")
            .addDefaultRequestHeader()
            .exchange()
            .expectStatus().isOk
            .expectBody<TestResponse<MyProfileResponse>>()
            .returnResult()
            .responseBody!!

        assertThat(result.payload).isEqualTo(
            MyProfileResponse(
                name = "홍길동",
                email = "test@test.com",
                profileImageUrl = null,
            ),
        )
    }
}
