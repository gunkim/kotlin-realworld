package io.github.gunkim.realworld.web.response

data class UserResponse(
    val email: String,
    val token: String,
    val username: String,
    val bio: String?,
    val image: String?,
)
