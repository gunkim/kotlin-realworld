package io.github.gunkim.realworld.application.user

import io.github.gunkim.realworld.domain.user.Email
import io.github.gunkim.realworld.domain.user.UserName

class UserRegistrationRequest(
    username: String,
    email: String,
    val password: String,
) {
    val username = UserName(username)
    val email = Email(email)
}