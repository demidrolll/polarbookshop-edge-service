package com.polarbookshop.service.edge.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository
import org.springframework.security.web.server.csrf.ServerCsrfTokenRequestAttributeHandler

@EnableWebFluxSecurity
@Configuration
class SecurityConfig {

  @Bean
  fun springSecurityFilterChain(
    http: ServerHttpSecurity,
    clientRegistrationRepository: ReactiveClientRegistrationRepository,
  ): SecurityWebFilterChain =
    http
      .authorizeExchange { exchange ->
        exchange
          .pathMatchers("/", "/*.css", "/*.js", "/favicon.ico").permitAll()
          .pathMatchers(HttpMethod.GET, "/books/**").permitAll()
          .anyExchange().authenticated()
      }
      .exceptionHandling { exceptionHandling ->
        exceptionHandling.authenticationEntryPoint(HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
      }
      .oauth2Login(Customizer.withDefaults())
      .logout { logout ->
        logout.logoutSuccessHandler(oidcLogoutSuccessHandler(clientRegistrationRepository))
      }
      .csrf { csrf ->
        csrf
          .csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
          .csrfTokenRequestHandler(ServerCsrfTokenRequestAttributeHandler())
      }
      .build()

  private fun oidcLogoutSuccessHandler(
    clientRegistrationRepository: ReactiveClientRegistrationRepository,
  ): ServerLogoutSuccessHandler =
    OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository)
      .also {
        it.setPostLogoutRedirectUri("{baseUrl}")
      }

  }
