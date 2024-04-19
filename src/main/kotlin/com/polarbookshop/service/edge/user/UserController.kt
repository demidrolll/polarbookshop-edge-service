package com.polarbookshop.service.edge.user

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class UserController {

  @GetMapping("user")
  fun getUser(@AuthenticationPrincipal oidcUser: OidcUser): Mono<User> =
    Mono.fromCallable {
      User(
        username = oidcUser.preferredUsername,
        firstName = oidcUser.givenName,
        lastName = oidcUser.familyName,
        roles = listOf("employee", "customer")
      )
    }
}
