package com.polarbookshop.service.edge.config

import org.springframework.context.annotation.Bean
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@EnableWebFluxSecurity
class SecurityConfig {

  @Bean
  fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
    http
      .authorizeExchange { exchange ->
        exchange.anyExchange().authenticated()
      }
      .formLogin(Customizer.withDefaults())
      .build()
}
