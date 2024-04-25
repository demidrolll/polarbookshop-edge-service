package com.polarbookshop.service.edge.config

import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@WebFluxTest
@Import(SecurityConfig::class)
class SecurityConfigTest(
  @Autowired
  private val webClient: WebTestClient
) {

  @MockBean
  lateinit var clientRegistrationRepository: ReactiveClientRegistrationRepository

  @Test
  fun `when logout authenticated and with csrf token then 302`() {
    given(clientRegistrationRepository.findByRegistrationId("test"))
      .willReturn(Mono.just(testClientRegistration()))

    webClient
      .mutateWith(SecurityMockServerConfigurers.mockOidcLogin())
      .mutateWith(SecurityMockServerConfigurers.csrf())
      .post()
      .uri("/logout")
      .exchange()
      .expectStatus().isFound()
  }

  private fun testClientRegistration() =
    ClientRegistration.withRegistrationId("test")
      .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
      .clientId("test")
      .authorizationUri("https://sso.polarbookshop.com/auth")
      .tokenUri("https://sso.polarbookshop.com/token")
      .redirectUri("https://polarbookshop.com")
      .build()
}
