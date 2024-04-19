package com.polarbookshop.service.edge.config

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.security.Principal

@Configuration
class RateLimiterConfig {

  @Bean
  fun keyResolver(): KeyResolver =
    KeyResolver { exchange ->
      exchange.getPrincipal<Principal>()
        .map(Principal::getName)
        .defaultIfEmpty("anonymous")
    }
}
