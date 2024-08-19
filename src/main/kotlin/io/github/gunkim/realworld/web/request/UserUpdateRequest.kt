package io.github.gunkim.realworld.web.request

import io.github.gunkim.realworld.domain.user.Email
import io.github.gunkim.realworld.domain.user.UserName

class UserUpdateRequest(
    email: String?,
    username: String?,
    val password: String?,
    val image: String?,
    val bio: String?,
) {
    val email = email?.let { Email(it) }
    val username = username?.let { UserName(it) }
}