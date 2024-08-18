package io.github.gunkim.realworld.application.user

data class UserAuthenticateRequest(
    val email: String,
    val password: String,
)