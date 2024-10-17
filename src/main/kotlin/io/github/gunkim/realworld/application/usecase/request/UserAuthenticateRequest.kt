package io.github.gunkim.realworld.application.usecase.request

import io.github.gunkim.realworld.domain.user.Email

class UserAuthenticateRequest(
    email: String,
    val password: String,
) {
    val email = Email(email)
}