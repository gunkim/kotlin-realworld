package io.github.gunkim.realworld.web.api.user.model.request

class UserUpdateRequest(
    val email: String?,
    val username: String?,
    val image: String?,
    val password: String?,
    val bio: String?,
)