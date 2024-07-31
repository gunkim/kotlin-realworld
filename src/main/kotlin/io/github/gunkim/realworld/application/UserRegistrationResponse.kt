package io.github.gunkim.realworld.application

data class UserRegistrationResponse(
    val username: String,
    val email: String,
    val password: String,
)