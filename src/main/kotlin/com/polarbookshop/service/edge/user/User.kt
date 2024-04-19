package com.polarbookshop.service.edge.user

data class User(
  val username: String,
  val firstName: String,
  val lastName: String,
  val roles: List<String>
)
