package io.github.gunkim.realworld.web.request

data class UserAuthenticateRequest(
    val email: String,
    val password: String,
)