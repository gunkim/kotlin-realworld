package io.github.gunkim.realworld.web.model

import io.github.gunkim.realworld.domain.user.UserId

data class AuthenticatedUser(
    val id: UserId,
)