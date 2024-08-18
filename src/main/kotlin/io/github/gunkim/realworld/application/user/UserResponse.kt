package io.github.gunkim.realworld.application.user

data class UserResponse(
    val email: String,
    val token: String,
    val username: String,
    val bio: String?,
    val image: String?,
)
